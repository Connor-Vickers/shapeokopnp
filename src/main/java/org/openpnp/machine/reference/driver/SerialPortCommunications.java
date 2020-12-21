package org.openpnp.machine.reference.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.pmw.tinylog.Logger;
import org.simpleframework.xml.Attribute;

import com.fazecast.jSerialComm.SerialPort;

/**
 * A class for SerialPort Communications. Includes functions for connecting,
 * disconnecting, reading and sending lines.
 */
public class SerialPortCommunications extends ReferenceDriverCommunications {
    public enum DataBits {
        Five(5),
        Six(6),
        Seven(7),
        Eight(8);

        public final int mask;

        private DataBits(int mask) {
            this.mask = mask;
        }
    }

    public enum StopBits {
        One(SerialPort.ONE_STOP_BIT),
        OnePointFive(SerialPort.ONE_POINT_FIVE_STOP_BITS),
        Two(SerialPort.TWO_STOP_BITS);

        public final int mask;

        private StopBits(int mask) {
            this.mask = mask;
        }
    }

    public enum FlowControl {
        Off(SerialPort.FLOW_CONTROL_DISABLED),
        RtsCts(SerialPort.FLOW_CONTROL_CTS_ENABLED | SerialPort.FLOW_CONTROL_RTS_ENABLED),
        XonXoff(SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED | SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED);

        public final int mask;

        private FlowControl(int mask) {
            this.mask = mask;
        }
    }

    public enum Parity {
        None(SerialPort.NO_PARITY),
        Mark(SerialPort.MARK_PARITY),
        Space(SerialPort.SPACE_PARITY),
        Even(SerialPort.EVEN_PARITY),
        Odd(SerialPort.ODD_PARITY);

        public final int mask;

        private Parity(int mask) {
            this.mask = mask;
        }
    }
    
    @Attribute(required = false)
    protected String portName2 = "";//todo set some how

    @Attribute(required = false)
    protected String portName = "";

    @Attribute(required = false)
    protected int baud = 115200;

    @Attribute(required = false)
    protected FlowControl flowControl = FlowControl.Off;

    @Attribute(required = false)
    protected DataBits dataBits = DataBits.Eight;

    @Attribute(required = false)
    protected StopBits stopBits = StopBits.One;

    @Attribute(required = false)
    protected Parity parity = Parity.None;

    @Attribute(required = false)
    protected boolean setDtr = false;

    @Attribute(required = false)
    protected boolean setRts = false;

    @Attribute(required = false)
    protected String name = "SerialPortCommunications";


    private SerialPort serialPort;
    private SerialPort serialPort2;
    
    public synchronized void connect2() throws Exception {
        disconnect2();
        serialPort2 = SerialPort.getCommPort(portName2);
        serialPort2.openPort(0);
        serialPort2.setComPortParameters(baud, dataBits.mask, stopBits.mask, parity.mask);
        serialPort2.setFlowControl(flowControl.mask);
        if (setDtr) {
            serialPort2.setDTR();
        }
        if (setRts) {
            serialPort2.setRTS();
        }
        serialPort2.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 500, 0);
    }

    public synchronized void disconnect2() throws Exception {
        if (serialPort2 != null && serialPort2.isOpen()) {
            serialPort2.closePort();
            serialPort2 = null;
        }
    }    
    

    public synchronized void connect() throws Exception {
        disconnect();
        serialPort = SerialPort.getCommPort(portName);
        serialPort.openPort(0);
        serialPort.setComPortParameters(baud, dataBits.mask, stopBits.mask, parity.mask);
        serialPort.setFlowControl(flowControl.mask);
        if (setDtr) {
            serialPort.setDTR();
        }
        if (setRts) {
            serialPort.setRTS();
        }
        serialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 500, 0);
    }

    public synchronized void disconnect() throws Exception {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            serialPort = null;
        }
    }


    /**
     * Returns an array of Strings containing the names of serial ports
     * present on the system
     * 
     * @return array of Strings of serial port names
     */
    public static String[] getPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        ArrayList<String> portNames = new ArrayList<>();
        for (SerialPort port : ports) {
            portNames.add(port.getSystemPortName());
        }
        return portNames.toArray(new String[] {});
    }

    public int read() throws TimeoutException, IOException {
        byte[] b = new byte[1];
        int l = serialPort.readBytes(b, 1);
        if (l == -1) {
            throw new IOException("Read error.");
        }
        if (l == 0) {
            throw new TimeoutException("Read timeout.");
        }
        return b[0];
    }

    public void writeBytes(byte[] data) throws IOException {
    	System.out.println(data);
        int l = serialPort.writeBytes(data, data.length);
        if (l == -1) {
            throw new IOException("Write error.");
        }
    }
    
    public void writeBytes2(byte[] data) throws IOException {
    	System.out.println(data);
        int l = serialPort2.writeBytes(data, data.length);
        if (l == -1) {
            throw new IOException("Write error 2.");
        }
    }
    
    public String getConnectionName() {
        return "serial://" + portName;
    }

    public String getConnectionName2() {
        return "serial://" + portName2;
    }
    
    public String getPortName() {
        return portName;
    }

    public String getPortName2() {
        return portName2;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public void setPortName2(String portName2) {
        this.portName2 = portName2;
    }
    
    public int getBaud() {
        return baud;
    }

    public void setBaud(int baud) {
        this.baud = baud;
    }

    public FlowControl getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(FlowControl flowControl) {
        this.flowControl = flowControl;
    }

    public DataBits getDataBits() {
        return dataBits;
    }

    public void setDataBits(DataBits dataBits) {
        this.dataBits = dataBits;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public boolean isSetDtr() {
        return setDtr;
    }

    public void setSetDtr(boolean setDtr) {
        this.setDtr = setDtr;
    }

    public boolean isSetRts() {
        return setRts;
    }

    public void setSetRts(boolean setRts) {
        this.setRts = setRts;
    }
}

