/*
 	Copyright (C) 2011 Jason von Nieda <jason@vonnieda.org>
 	
 	This file is part of OpenPnP.
 	
	OpenPnP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenPnP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenPnP.  If not, see <http://www.gnu.org/licenses/>.
 	
 	For more information about OpenPnP visit http://openpnp.org
 */

package org.openpnp;

import org.openpnp.Board.Side;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Persist;


/**
 * A Placement describes a location on a Board where a Part will be placed, 
 * along with information about how to place it. 
 * @author jason
 */
public class Placement implements RequiresConfigurationResolution {
	@Attribute
	private String id;
	@Element
	private Location location;
	@Attribute
	private Side side;
	private Part part;
	
	@Attribute
	private String partId;
	
	@Override
	public void resolve(Configuration configuration) throws Exception {
		part = configuration.getPart(partId);
	}
	
	@SuppressWarnings("unused")
	@Persist
	private void persist() {
		partId = (part == null ? null : part.getId());
	}
	
	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}
}
