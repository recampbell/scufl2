/*******************************************************************************
 * Copyright (C) 2010 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package uk.org.taverna.scufl2.api.configurations;

import java.net.URI;
import java.util.LinkedHashSet;

import uk.org.taverna.scufl2.api.property.PropertyLiteral;

/**
 * The definition of a {@link PropertyLiteral}.
 *
 * @author David Withers
 * @author Stian Soiland-Reyes
 */
public class PropertyLiteralDefinition extends PropertyDefinition {
	private LinkedHashSet<String> options;

	private URI literalType;

	/**
	 * Creates a definition of a <code>PropertyLiteral</code>.
	 */
	public PropertyLiteralDefinition() {
	}

	/**
	 * Creates a definition of a <code>PropertyLiteral</code> with no
	 * constraints on the property value.
	 *
	 * @param predicate
	 *            the URI identifying the <code>PropertyLiteral</code> that this
	 *            class defines
	 * @param literalType
	 *            the literalType of the <code>PropertyLiteral</code>
	 * @param name
	 *            the name of the <code>PropertyLiteral</code>
	 * @param label
	 *            a human readable label for the <code>PropertyLiteral</code>
	 * @param description
	 *            a description of the <code>PropertyLiteral</code>
	 * @param required
	 *            whether the <code>PropertyLiteral</code> is mandatory
	 * @param multiple
	 *            whether there can be multiple instances of the
	 *            <code>PropertyLiteral</code>
	 * @param ordered
	 *            whether the order of multiple instances of the
	 *            <code>Property</code> is significant
	 */
	public PropertyLiteralDefinition(URI predicate, URI literalType,
			String name, String label, String description, boolean required,
			boolean multiple, boolean ordered) {
		super(predicate, name, label, description, required, multiple, ordered);
		this.literalType = literalType;
		options = new LinkedHashSet<String>();
	}

	/**
	 * Creates a definition of a <code>PropertyLiteral</code>.
	 *
	 * @param predicate
	 *            the URI identifying the <code>PropertyLiteral</code> that this
	 *            class defines
	 * @param literalType
	 *            the literalType of the <code>PropertyLiteral</code>
	 * @param name
	 *            the name of the <code>PropertyLiteral</code>
	 * @param label
	 *            a human readable label for the <code>PropertyLiteral</code>
	 * @param description
	 *            a description of the <code>PropertyLiteral</code>
	 * @param required
	 *            whether the <code>PropertyLiteral</code> is mandatory
	 * @param multiple
	 *            whether there can be multiple instances of the
	 *            <code>PropertyLiteral</code>
	 * @param ordered
	 *            whether the order of multiple instances of the
	 *            <code>Property</code> is significant
	 * @param options
	 *            the valid values for the <code>PropertyLiteral</code>
	 */
	public PropertyLiteralDefinition(URI predicate, URI literalType,
			String name, String label, String description, boolean required,
			boolean multiple, boolean ordered, LinkedHashSet<String> options) {
		super(predicate, name, label, description, required, multiple, ordered);
		this.literalType = literalType;
		this.options = options;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PropertyLiteralDefinition other = (PropertyLiteralDefinition) obj;
		if (literalType == null) {
			if (other.literalType != null) {
				return false;
			}
		} else if (!literalType.equals(other.literalType)) {
			return false;
		}
		if (options == null) {
			if (other.options != null) {
				return false;
			}
		} else if (!options.equals(other.options)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the literalType of the <code>PropertyLiteral</code>.
	 *
	 * @return the literalType of the <code>PropertyLiteral</code>
	 */
	public URI getLiteralType() {
		return literalType;
	}

	/**
	 * Returns the valid values for the <code>Property</code>.
	 *
	 * If the value of the <code>Property</code> is not constrained this method
	 * will return an zero length array.
	 *
	 * @return the valid values for the <code>Property</code>
	 */
	public LinkedHashSet<String> getOptions() {
		return options;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (literalType == null ? 0 : literalType.hashCode());
		result = prime * result + (options == null ? 0 : options.hashCode());
		return result;
	}

	/**
	 * Sets the literalType of the <code>PropertyLiteral</code>.
	 *
	 * @param literalType
	 *            the literalType of the <code>PropertyLiteral</code>
	 */
	public void setLiteralType(URI literalType) {
		this.literalType = literalType;
	}

	/**
	 * Sets the valid values for the <code>Property</code>.
	 *
	 * @param options
	 */
	public void setOptions(LinkedHashSet<String> options) {
		this.options = options;
	}

	@Override
	protected String toString(String indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent);
		sb.append("PropertyLiteralDefinition ");
		if (getPredicate() != null) sb.append(getPredicate());
		sb.append("\n");
		sb.append(indent + "  label=" + getLabel() + ", description=" + getDescription() + "\n");
		sb.append(indent + "  required=" + isRequired() + ", multiple=" + isMultiple()+ ", ordered=" + isOrdered() + "\n");
		if (getOptions().size() > 0) {
			sb.append(indent + "  options=" + getOptions() + "\n");
		}
		sb.append(indent + "  literalType=" + getLiteralType() + "\n");
		return sb.toString();
	}
	
}
