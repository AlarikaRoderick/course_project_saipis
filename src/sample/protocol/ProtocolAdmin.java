package sample.protocol;

import sample.Admin.Admin;
import sample.User.User;

import java.io.IOException;
import java.io.InputStream;

public class ProtocolAdmin {
    public static byte[] serializeAdmin(Admin admin){
        byte[] adminLoginSerialized = serializeString(admin.getLogin());
        byte[] adminPasswordSerialized = serializeString(admin.getPassword());

        int packageLength = adminLoginSerialized.length + adminPasswordSerialized.length;

        byte[] length = getBytes(packageLength);
        byte[] data = new byte[4+packageLength+1];

        data[0] = length[0];
        data[1] = length[1];
        data[2] = length[2];
        data[3] = length[3];

        System.arraycopy(adminLoginSerialized, 0, data, 4, adminLoginSerialized.length);
        System.arraycopy(adminPasswordSerialized, 0, data, 4 + adminLoginSerialized.length, adminPasswordSerialized.length);

        data[data.length-1] = getCrc(data, 4, data.length-1);

        return data;
    }

    public static byte getCrc(byte[] data, int start, int stop) {
        byte crc = 0;
        for (int i = start; i < stop; i++) {
            crc = (byte) (crc + data[i]);
        }
        return crc;
    }

    public static byte[] getBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i>>24);
        result[1] = (byte) (i>>16);
        result[2] = (byte) (i>>8);
        result[3] = (byte) (i);

        return result;
    }

    public static byte[] serializeString(String str) {
        byte[] strMass = new byte[4+str.getBytes().length];
        byte[] length = getBytes(str.getBytes().length);

        strMass[0] = length[0];
        strMass[1] = length[1];
        strMass[2] = length[2];
        strMass[3] = length[3];

        System.arraycopy(str.getBytes(), 0, strMass, 4, str.getBytes().length);
        return strMass;
    }

    public static Admin deserializeAdmin(InputStream inputStream) throws IOException {
        byte[] length = new byte[4];
        inputStream.read(length);
        byte[] payLoad = new byte[ProtocolAdmin.byteToIntAdmin(length)];
        inputStream.read(payLoad);

        byte[] crc = new byte[1];
        inputStream.read(crc);

        return deserializeAdmin(payLoad);
    }

    public static Admin deserializeAdmin(byte[] payLoad){
        Admin admin = new Admin();

        int adminLoginSerializedLength = bytesToInt(payLoad[0], payLoad[1], payLoad[2], payLoad[3]);
        byte[] adminLoginSerializedData = new byte[adminLoginSerializedLength];

        System.arraycopy(payLoad, 4, adminLoginSerializedData, 0, adminLoginSerializedLength);

        int adminPasswordSerializedLength = bytesToInt(payLoad[4+adminLoginSerializedLength],
                payLoad[4+adminLoginSerializedLength+1],
                payLoad[4+adminLoginSerializedLength+2],
                payLoad[4+adminLoginSerializedLength+3]);

        byte[] adminPasswordSerializedData = new byte[adminPasswordSerializedLength];

        System.arraycopy(payLoad, 4+4+adminLoginSerializedLength, adminPasswordSerializedData, 0, adminPasswordSerializedLength);

        admin.setLogin(new String(adminLoginSerializedData));
        admin.setPassword(new String(adminPasswordSerializedData));

        return admin;
    }

    public static int bytesToInt(byte b0, byte b1, byte b2, byte b3) {
        return b3 & 0xFF | (b2 & 0xFF) << 8 | (b1 & 0xFF) << 16 | (b0 & 0xFF) << 24;
    }

    private static int byteToIntAdmin(byte[] length) {
        return length[3] & 0xFF | (length[2] & 0xFF) << 8 | (length[1] & 0xFF) << 16 | (length[0] & 0xFF) << 24;
    }
}
