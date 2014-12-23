/*
 * 
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package org.riversun.fcm.util;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Log Formatter
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class LogFormatter extends Formatter {

	private Calendar mCalendar = Calendar.getInstance();

	public synchronized String format(LogRecord record) {

		final String sourceClassName = record.getSourceClassName();
		final String sourceMethodName = record.getSourceMethodName();

		mCalendar.setTimeInMillis(record.getMillis());

		final StringBuilder sb = new StringBuilder();
		sb.append(String.format("%1$tD %1$tT.%1$tL [%2$6s] ", mCalendar, record.getLevel().toString()));

		if (sourceClassName != null) {
			sb.append(sourceClassName);
		} else {
			String loggerName = record.getLoggerName();
			sb.append(loggerName);
		}
		sb.append(" ");

		if (sourceMethodName != null) {
			sb.append(String.format("#%s", sourceMethodName));
		}
		sb.append(" ");
		sb.append(formatMessage(record));
		sb.append("\n");
		Throwable thrown = record.getThrown();
		if (thrown != null) {
			sb.append(record.getThrown());
		}
		return sb.toString();
	}
}