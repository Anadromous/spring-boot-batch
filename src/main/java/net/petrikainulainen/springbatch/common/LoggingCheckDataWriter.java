package net.petrikainulainen.springbatch.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import net.petrikainulainen.springbatch.student.RawBankCheckingData;

public class LoggingCheckDataWriter implements ItemWriter<RawBankCheckingData> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCheckDataProcessor.class);

	@Override
	public void write(List<? extends RawBankCheckingData> items) throws Exception {
		LOGGER.info("--> Received the information for {} rawcheckdata items.", items.size());

        items.forEach(i -> LOGGER.debug("--> Received the information for rawcheckdata: {}", i));
		
	}

}
