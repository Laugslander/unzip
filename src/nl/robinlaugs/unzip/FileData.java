package nl.robinlaugs.unzip;

public class FileData {

    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("FileData{name='%s', data='%s'}", this.name, new String(this.data));
    }
}
