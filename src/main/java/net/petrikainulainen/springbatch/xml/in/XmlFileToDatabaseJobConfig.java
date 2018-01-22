package net.petrikainulainen.springbatch.xml.in;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import net.petrikainulainen.springbatch.common.LoggingCheckDataProcessor;
import net.petrikainulainen.springbatch.student.RawBankCheckingData;
import net.petrikainulainen.springbatch.student.StudentDTO;

/**
 * @author Peter Chapman
 */
@Configuration
public class XmlFileToDatabaseJobConfig {
    
	private static final String PROPERTY_CSV_SOURCE_FILE_PATH = "xml.to.database.check.job.source.file.path";
    private static final String QUERY_INSERT_RAW_DATA = "insert into raw_check_data (trans_id, posting_date, effective_date, trans_type, amount, check_number, "
    				+ "ref_number, description, trans_category, type, balance) "
    				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    @Bean
    ItemReader<RawBankCheckingData> xmlFileItemReader(Environment environment) {
        StaxEventItemReader<RawBankCheckingData> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_CSV_SOURCE_FILE_PATH)));
        xmlFileReader.setFragmentRootElementName("student");

        Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
        studentMarshaller.setClassesToBeBound(StudentDTO.class);

        xmlFileReader.setUnmarshaller(studentMarshaller);
        return xmlFileReader;
    }

    @Bean
    ItemProcessor<RawBankCheckingData, RawBankCheckingData> xmlFileItemProcessor() {
        return new LoggingCheckDataProcessor();
    }

    @Bean
    ItemWriter<RawBankCheckingData> xmlFileDatabaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<RawBankCheckingData> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql(QUERY_INSERT_RAW_DATA);

        ItemPreparedStatementSetter<RawBankCheckingData> checkDataPreparedStatementSetter = new CheckDataPreparedStatementSetter();
        databaseItemWriter.setItemPreparedStatementSetter(checkDataPreparedStatementSetter);

        return databaseItemWriter;
    }
    @Bean
    Step xmlFileToDatabaseStep(ItemReader<RawBankCheckingData> xmlFileItemReader,
                               ItemProcessor<RawBankCheckingData, RawBankCheckingData> xmlFileItemProcessor,
                               ItemWriter<RawBankCheckingData> xmlFileDatabaseItemWriter,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("xmlFileToDatabaseStep")
                .<RawBankCheckingData, RawBankCheckingData>chunk(1)
                .reader(xmlFileItemReader)
                .processor(xmlFileItemProcessor)
                .writer(xmlFileDatabaseItemWriter)
                .build();
    }

    @Bean
    Job xmlFileToDatabaseJob(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("xmlFileToDatabaseStep") Step xmlStudentStep) {
        return jobBuilderFactory.get("xmlFileToDatabaseJob")
                .incrementer(new RunIdIncrementer())
                .flow(xmlStudentStep)
                .end()
                .build();
    }
}
