/*
 * Copyright (c) 2017
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

	private static final boolean DEBUG = true;

	private static LogHelper instance = null;

	private Logger logger;

	private LogHelper() {
		try {
			logger = Logger.getLogger(LogHelper.class.getName());

			if ( DEBUG )
				logger.setLevel(Level.ALL);
			else
				logger.setLevel(Level.WARNING);

			Handler handler = new FileHandler("%t/suji.log", 0, 1, false);
			logger.addHandler(handler);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void info(String message) {
		getLogger().info(message);
	}

	public static Logger getLogger() {
		return getInstance().logger;
	}

	private static LogHelper getInstance() {
		if ( instance == null ) {
			synchronized (LogHelper.class) {
				if ( instance == null ) {
					instance = new LogHelper();
					instance.logger.finest("Logger initialized.");
				}
			}
		}

		return instance;
	}

	public static void finest(String message) {
		getLogger().finest(message);
	}

	public static void log(Level level, String msg, Throwable thrown) {
		getLogger().log(level, msg, thrown);
	}
}
