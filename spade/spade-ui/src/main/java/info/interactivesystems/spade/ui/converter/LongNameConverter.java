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
package info.interactivesystems.spade.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;

/**
 * Caps long Strings.
 *
 * @author Dennis Rippinger
 */
@FacesConverter(value = "converter.longName")
public class LongNameConverter implements Converter, Serializable {

	private static final long serialVersionUID = 2479669304984009829L;

	private static final int MAXIMUM = 35;

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String productName = (String) value;

		if (productName.length() > MAXIMUM) {
			productName = productName.substring(0, MAXIMUM);
			productName += " ...";
		}

		return productName;

	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// Direction not needed
		return null;
	}
}