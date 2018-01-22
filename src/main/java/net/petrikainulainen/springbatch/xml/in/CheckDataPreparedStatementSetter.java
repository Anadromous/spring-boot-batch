package net.petrikainulainen.springbatch.xml.in;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import net.petrikainulainen.springbatch.student.RawBankCheckingData;
import net.petrikainulainen.springbatch.student.StudentDTO;

/**
 * This class is used to set the actual parameter values of a
 * prepared statement.
 */
final class CheckDataPreparedStatementSetter implements ItemPreparedStatementSetter<RawBankCheckingData> {

    @Override
    public void setValues(RawBankCheckingData checkData, PreparedStatement preparedStatement) throws SQLException {
    	
        preparedStatement.setString(1, checkData.getTransactionId());
        preparedStatement.setDate(2,  new java.sql.Date(checkData.getPostingDate().getTime()));
        preparedStatement.setDate(3, new java.sql.Date(checkData.getEffectiveDate().getTime()));
        preparedStatement.setString(4, checkData.getTransactionType());
        preparedStatement.setDouble(5, checkData.getAmount());
        preparedStatement.setLong(6, checkData.getCheckNumber());
        preparedStatement.setString(7, checkData.getReferenceNumber());
        preparedStatement.setString(8, checkData.getDescription());
        preparedStatement.setString(9, checkData.getTransactionCategory());
        preparedStatement.setString(10, checkData.getType());
        preparedStatement.setDouble(11, checkData.getBalance());
    }
}
