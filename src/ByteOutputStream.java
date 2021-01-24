import java.io.*;

class ByteOutputStream extends FileOutputStream {

    byte buffer;
    byte pos;

    public ByteOutputStream(File file) throws FileNotFoundException {
        super(file);
        buffer = 0x00;
    }

    public void write(byte bit) throws IOException {
        if(!(bit == 0 || bit == 1))
            throw new RuntimeException("A bit can only be a 1 or 0");

        buffer = (byte)(buffer << 1);
        buffer |= bit;

        pos++;

        if(pos == 8)
            flushByte();
    }

    public void write(byte[] bits) throws IOException {
        for(byte bit : bits)
            write(bit);
    }

    public void close() throws IOException {
        if(pos != 0) {
            buffer = (byte)(buffer << (8 - pos));
            flushByte();
        }
        super.close();
    }

    private void flushByte() throws IOException {
        super.write(buffer);
        buffer = 0x00;
        pos = 0;
    }

}