package net.petrikainulainen.springbatch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import net.petrikainulainen.springbatch.student.RawBankCheckingData;

public class LoggingCheckDataProcessor implements ItemProcessor<RawBankCheckingData, RawBankCheckingData> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCheckDataProcessor.class);
	
	@Override
	public RawBankCheckingData process(RawBankCheckingData item) throws Exception {
		LOGGER.info("=============================================================");
		LOGGER.info("Processing rawcheckdata information: {}", item);
		return item;
	}

}
