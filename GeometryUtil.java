package vn.com.vng.mcrusprofile.util;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

/**
 * @author sonnd
 *
 */
public class GeometryUtil {

	public static Point<G2D> createPoint(double longitude, double latitude) {
		Point<G2D> point = Geometries.mkPoint(new G2D(longitude, latitude), CoordinateReferenceSystems.WGS84);
		return point;
	}

	public static Point<G2D> createPoint(String longitude, String latitude) {
		double dlongitude = Double.parseDouble(longitude);
		double dlatitude = Double.parseDouble(latitude);
		Point<G2D> point = Geometries.mkPoint(new G2D(dlongitude, dlatitude), CoordinateReferenceSystems.WGS84);
		return point;
	}
}
