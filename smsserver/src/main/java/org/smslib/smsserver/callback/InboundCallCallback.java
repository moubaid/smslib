
package org.smslib.smsserver.callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.callback.IInboundCallCallback;
import org.smslib.callback.events.InboundCallEvent;
import org.smslib.smsserver.SMSServer;

public class InboundCallCallback implements IInboundCallCallback
{
	static Logger logger = LoggerFactory.getLogger(InboundCallCallback.class);

	@Override
	public boolean process(InboundCallEvent event)
	{
		Connection db = null;
		try
		{
			db = SMSServer.getInstance().getDbConnection();
			PreparedStatement s = db.prepareStatement("insert into smslib_calls (date, caller_id, gateway_id) values (?, ?, ?)");
			s.setTimestamp(1, new Timestamp(event.getDate().getTime()));
			s.setString(2, event.getMsisdn().getNumber());
			s.setString(3, event.getGatewayId());
			s.executeUpdate();
			s.close();
			db.commit();
			return true;
		}
		catch (Exception e)
		{
			logger.error("Error!", e);
			return false;
		}
		finally
		{
			if (db != null)
			{
				try
				{
					db.close();
				}
				catch (SQLException e)
				{
					logger.error("Error!", e);
				}
			}
		}
	}
}
