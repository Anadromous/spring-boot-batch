/**
 * @author Peter Chapman / pchapman@easystreet.net
 */
package net.petrikainulainen.springbatch.csv.in;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import net.petrikainulainen.springbatch.common.LoggingCheckDataProcessor;
import net.petrikainulainen.springbatch.student.RawBankCheckingData;

/**
 * @author Peter Chapman / pchapman@easystreet.net
 *
 */
@Configuration
public class CsvFileToDatabaseCheckDataJobConfig {
	
	private static final String PROPERTY_CSV_SOURCE_FILE_PATH = "csv.to.database.check.job.source.file.path";
    private static final String QUERY_INSERT_RAW_DATA = "insert into raw_data (trans_id, posting_date, effective_date, trans_type, amount, check_number, "
    				+ "ref_number, description, trans_category, type, balance) "
    				+ "values (:transactionId, :postingDate, :effectiveDate, :transactionType, :amount, :checkNumber, "
    				+ ":referenceNumber, :description, :transactionCategory, :type, :balance);";
    
    @Bean
    ItemReader<RawBankCheckingData> csvFileItemReader(Environment environment) {
        FlatFileItemReader<RawBankCheckingData> csvFileReader = new FlatFileItemReader<>();
        csvFileReader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_CSV_SOURCE_FILE_PATH)));
        csvFileReader.setLinesToSkip(1);

        LineMapper<RawBankCheckingData> checkDataLineMapper = createCheckDataLineMapper();
        csvFileReader.setLineMapper(checkDataLineMapper);

        return csvFileReader;
    }

    private LineMapper<RawBankCheckingData> createCheckDataLineMapper() {
        DefaultLineMapper<RawBankCheckingData> checkDataLineMapper = new DefaultLineMapper<>();

        LineTokenizer checkDataLineTokenizer = createCheckDataLineTokenizer();
        checkDataLineMapper.setLineTokenizer(checkDataLineTokenizer);

        FieldSetMapper<RawBankCheckingData> studentInformationMapper = createCheckDataInformationMapper();
        checkDataLineMapper.setFieldSetMapper(studentInformationMapper);

        return checkDataLineMapper;
    }

    private LineTokenizer createCheckDataLineTokenizer() {
        DelimitedLineTokenizer checkDataLineTokenizer = new DelimitedLineTokenizer();
        checkDataLineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        //Transaction ID,Posting Date,Effective Date,Transaction Type,Amount,Check Number,Reference Number,Description,Transaction Category,Type,Balance
        checkDataLineTokenizer.setNames(new String[]{"transactionId", "postingDate", "effectiveDate", "transactionType", "amount",
        		"checkNumber", "referenceNumber", "description", "transactionCategory", "type", "balance"});
        return checkDataLineTokenizer;
    }

    private FieldSetMapper<RawBankCheckingData> createCheckDataInformationMapper() {
        BeanWrapperFieldSetMapper<RawBankCheckingData> checkDataInformationWrapper = new BeanWrapperFieldSetMapper<>();
        checkDataInformationWrapper.setTargetType(RawBankCheckingData.class);
        return checkDataInformationWrapper;
    }

    @Bean
    ItemProcessor<RawBankCheckingData, RawBankCheckingData> csvFileItemProcessor() {
        return new LoggingCheckDataProcessor();
    }

    @Bean
    ItemWriter<RawBankCheckingData> csvFileDatabaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<RawBankCheckingData> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql(QUERY_INSERT_RAW_DATA);

        ItemSqlParameterSourceProvider<RawBankCheckingData> sqlParameterSourceProvider = studentSqlParameterSourceProvider();
        databaseItemWriter.setItemSqlParameterSourceProvider(sqlParameterSourceProvider);

        return databaseItemWriter;
    }

    private ItemSqlParameterSourceProvider<RawBankCheckingData> studentSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<>();
    }

    @Bean
    Step csvFileToDatabaseCheckDataStep(ItemReader<RawBankCheckingData> csvFileItemReader,
                               ItemProcessor<RawBankCheckingData, RawBankCheckingData> csvFileItemProcessor,
                               ItemWriter<RawBankCheckingData> csvFileDatabaseItemWriter,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("csvFileToDatabaseCheckDataStep")
                .<RawBankCheckingData, RawBankCheckingData>chunk(1)
                .reader(csvFileItemReader)
                .processor(csvFileItemProcessor)
                .writer(csvFileDatabaseItemWriter)
                .build();
    }

    @Bean
    Job csvFileToDatabaseCheckDataJob(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("csvFileToDatabaseCheckDataStep") Step csvCheckDataStep) {
        return jobBuilderFactory.get("csvFileToDatabaseCheckDataJob")
                .incrementer(new RunIdIncrementer())
                .flow(csvCheckDataStep)
                .end()
                .build();
    }
}
