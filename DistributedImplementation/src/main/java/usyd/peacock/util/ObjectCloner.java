package usyd.peacock.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectCloner {

	public static Object deepCopy(Object oldObj) {

		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(oldObj);
			oos.flush();
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);
			return ois.readObject();

		} catch (Exception e) {

			System.out.println("Exception in ObjectCloner = " + e);

		} finally {
			try {
				oos.close();
				ois.close();
			} catch (Exception ex) {

			}
		}
		
		return null;

	}
}
