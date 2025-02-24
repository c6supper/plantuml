/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.real;

import net.sourceforge.plantuml.log.Logme;

class PositiveForce {

	private final Real fixedPoint;
	private final RealMoveable movingPoint;
	private final double minimunDistance;
	private final boolean trace = false;
	private final Throwable creationPoint;

	public PositiveForce(Real fixedPoint, RealMoveable movingPoint, double minimunDistance) {
		if (fixedPoint == movingPoint) {
			throw new IllegalArgumentException();
		}
		this.fixedPoint = fixedPoint;
		this.movingPoint = movingPoint;
		this.minimunDistance = minimunDistance;
		this.creationPoint = new Throwable();
		this.creationPoint.fillInStackTrace();
	}

	@Override
	public String toString() {
		return "PositiveForce fixed=" + fixedPoint + " moving=" + movingPoint + " min=" + minimunDistance;
	}

	public boolean apply() {
		if (trace) {
			System.err.println("apply " + this);
		}
		final double movingPointValue = movingPoint.getCurrentValue();
		final double fixedPointValue;
		try {
			fixedPointValue = fixedPoint.getCurrentValue();
		} catch (IllegalStateException e) {
			System.err.println("Pb with force " + this);
			System.err.println("This force has been created here:");
			Logme.error(creationPoint);
			System.err.println("The fixed point has been created here: " + fixedPoint);
			fixedPoint.printCreationStackTrace();
			throw e;
		}
		final double distance = movingPointValue - fixedPointValue;
		final double diff = distance - minimunDistance;
		if (diff >= 0) {
			if (trace) {
				System.err.println("Not using ");
			}
			return false;
		}
		if (trace) {
			System.err.println("moving " + (-diff) + " " + movingPoint);
		}
		movingPoint.move(-diff);
		return true;
	}

}
