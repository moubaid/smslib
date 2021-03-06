
package org.smslib.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.smslib.gateway.AbstractGateway;
import org.smslib.message.OutboundMessage;

public class NumberRouter extends AbstractRouter
{
	Map<String, AbstractGateway> rules = new HashMap<String, AbstractGateway>();

	public Map<String, AbstractGateway> getRules()
	{
		return this.rules;
	}

	public void addRule(String pattern, AbstractGateway gateway)
	{
		getRules().put(pattern, gateway);
	}

	public void deleteRule(String pattern)
	{
		getRules().remove(pattern);
	}

	@Override
	public Collection<AbstractGateway> customRoute(OutboundMessage message, Collection<AbstractGateway> gateways)
	{
		Collection<AbstractGateway> candidates = new ArrayList<AbstractGateway>();
		if (getRules().size() != 0)
		{
			Set<String> r = getRules().keySet();
			for (String rx : r)
			{
				Pattern p = Pattern.compile(rx);
				Matcher m = p.matcher(message.getRecipient().getNumber());
				if (m.matches() && gateways.contains(getRules().get(rx))) candidates.add(getRules().get(rx));
			}
		}
		return candidates;
	}
}
