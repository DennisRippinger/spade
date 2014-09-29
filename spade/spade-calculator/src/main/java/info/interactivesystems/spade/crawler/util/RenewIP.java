/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.crawler.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Renews the IP on FRITZ.BOX router.
 *
 * @author Dennis Rippinger
 */
@Slf4j
public final class RenewIP {

	/**
	 * Private Constructor.
	 */
	private RenewIP() {

	}

	/**
	 * Renew dynamic IP on Fritz Boxes.
	 */
	public static void renewIpOnFritzBox() {
		try {
			String xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<s:Body>"
					+ "<u:ForceTermination xmlns:u=\"urn:schemas-upnp-org:service:WANIPConnection:1\" />"
					+ "</s:Body>" + "</s:Envelope>";

			// Create socket
			String hostname = "fritz.box";
			int port = 49000;
			InetAddress addr = InetAddress.getByName(hostname);

			try (Socket sock = new Socket(addr, port);) {
				// Send header
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
						sock.getOutputStream(), "UTF-8"));
				wr.write("POST /upnp/control/WANIPConn1 HTTP/1.1");
				wr.write("Host: fritz.box:49000" + "\r\n");
				wr.write("SOAPACTION: \"urn:schemas-upnp-org:service:WANIPConnection:1#ForceTermination\""
						+ "\r\n");
				wr.write("Content-Type: text/xml; charset=\"utf-8\"" + "\r\n");
				wr.write("Content-Length: " + xmldata.length() + "\r\n");
				wr.write("\r\n");

				// Send data
				wr.write(xmldata);
				wr.flush();
			}

		} catch (Exception e) {
			log.error("Could not send Renew IP Command", e);
		}

		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			log.error("Interruped", e);
		}

	}
}
