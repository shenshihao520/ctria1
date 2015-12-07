package com.fntech.m10.u1.rfid.reader;

import java.io.IOException;
import java.util.Map;


import com.fntech.m10.u1.model.Message;
import com.fntech.m10.u1.rfid.command.Command;
import com.fntech.m10.u1.rfid.utils.StringUtils;

public class Manager implements IManager {

    private static final int f_s = 1;
    private static final int f_m = 3;
    private static final int to = 500;

    private String strEpc;
    private static Manager manager;
    private ReaderAndWriter raw;
    private Message message = new Message();

    public static Manager getInstance() throws SecurityException, IOException {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }

    private Manager() throws SecurityException, IOException {
        raw = ReaderAndWriter.getInstance();
        setTimeout(1000);
    }

    /**
     * 暂停
     */
    public void suspend() {
        raw.suspendWorker();
    }

    /**
     * 恢复
     */
    public void resume() {
        raw.resumeWorker();
    }

    @Override
    public void release() {
        raw.release();
        manager = null;
    }

    public void setTimeout(int ms) {
        if (raw != null) {
            raw.setTimeout(ms);
        }
    }

    /**
     * 直接发送命令
     *
     * @param cmd
     */
    public String sendCmd(String cmd) {
        if (StringUtils.isBlank(cmd)) {
            return null;
        }
        return WriteAndReadComm(cmd.getBytes(), null);
        //raw.sendCmd(cmd, null);
    }

    @Override
    public boolean openInterface() {
        /*byte[] cmd = null;
		raw.setEndType(endType);
		if(endType == 1){
			cmd = Command.OPEN_INTERFACE_1;
		}else{
		
		}
		cmd = ;*/
        String strRET = WriteAndReadComm(Command.OPEN_INTERFACE_2, null);
        return strRET.equals("true");
        //raw.sendCmd(Command.OPEN_INTERFACE_2);

    }

    @Override
    public String checkConnectivity() {
        //byte[] cmd = null;
		/*if(raw.getEndType() == 1){
			cmd = Command.NULL_1;
		}else
		{*/
        //cmd = ;
        //}

        return WriteAndReadComm(Command.NULL_2, null);
        //raw.sendCmd(Command.NULL_2);
    }

    @Override
    public String cmdInventory() {
        String[] params = {
                1 + "",
                0 + "",
                0 + ""
        };
        return WriteAndReadComm(Command.CMD_INVENTORY.getBytes(), params);
    }

    @Override
    public String inventory() {
        String[] params = {
                0 + "",
                0 + "",
                500 + ""
        };
        return WriteAndReadComm(Command.CMD_INVENTORY.getBytes(), params);
        //raw.sendCmd(Command.CMD_INVENTORY, params);
    }

    @Override
    public boolean stopOperation() {
        WriteAndReadComm(Command.CMD_STOP.getBytes(), null);
        return true;
        //raw.sendCmd(Command.CMD_STOP, null);
    }

    public String getVersion() {
        return WriteAndReadComm(Command.CMD_GET_VERSION.getBytes(), null);
        //raw.sendCmd(Command.CMD_GET_VERSION, null);
    }

    public String setDefaultParameter() {
        return WriteAndReadComm(Command.CMD_SET_DEFAULT_PARAMETER.getBytes(), null);
        //raw.sendCmd(Command.CMD_SET_DEFAULT_PARAMETER, null);
    }

    public String inventoryParameter(int session, int q, int m_ab) {
        if (session < 0 || session > 3) {
            return null;
        }
        if (q < 0 || q > 15) {
            return null;
        }
        if (m_ab < 0 || m_ab > 2) {
            return null;
        }
        String[] params = {
                session + "",
                q + "",
                m_ab + ""
        };
        return WriteAndReadComm(Command.CMD_INVENTORY_PARAMETER.getBytes(), params);
        //raw.sendCmd(Command.CMD_INVENTORY_PARAMETER, params);
    }

    public String getParameter(String cmd, String p) {
        String[] params = {
                cmd,
                p
        };
        return WriteAndReadComm(Command.CMD_GET_PARAMETER.getBytes(), params);
        //raw.sendCmd(Command.CMD_GET_PARAMETER, params);
    }

    @SuppressWarnings("null")
    public Message selectMask(
            int n,
            int bits,
            int mem,
            int b_offset,
            String pattern,
            int target,
            int action) {
        strEpc = pattern;

        if (n < 0 || n > 7) {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }
        if (mem < 0 || mem > 3) {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }
        if (b_offset < 0) {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }
        if (target < 0 || target > 4) {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }
        if (action < 0 || action > 7) {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }

        String[] params = {
                n + "",
                bits + "",
                mem + "",
                b_offset + "",
                pattern,
                target + "",
                action + ""
        };
        String result = WriteAndReadComm(Command.CMD_SELECT_MASK.getBytes(), params);
        if (result != null && !result.contains("err") && result.startsWith("ok")) {
            message.setCode(0);
            message.setMessage("Select Mask Successful!");
            message.setResult(result);
            return message;
        } else {
            message.setCode(1);
            message.setMessage("Select Mask Failure!");
            message.setResult(null);
            return message;
        }

        //raw.sendCmd(Command.CMD_SELECT_MASK, params);
    }

    @Override
    public String setTxPower(int a) {
		/*if(a>0)
		{
			a= 0;
		}*/
        return WriteAndReadComm(Command.CMD_SET_TX_POWER.getBytes(), new String[]{a + ""});
        //raw.sendCmd(Command.CMD_SET_TX_POWER, new String[]{a+""});
    }

    @Override
    public String getMaxPower() {
        return WriteAndReadComm(Command.CMD_GET_MAX_POWER.getBytes(), null);
        //raw.sendCmd(Command.CMD_GET_MAX_POWER, null);
    }

    @Override
    public String setTxCycle(int on, int off) {
        String[] params = {
                on + "",
                off + ""
        };
        return WriteAndReadComm(Command.CMD_SET_TX_CYCLE.getBytes(), params);
        //raw.sendCmd(Command.CMD_SET_TX_CYCLE, params);
    }

    @Override
    public String changeChannelState(int channel, int f_e) {
        String[] params = {
                channel + "",
                f_e + ""
        };
        return WriteAndReadComm(Command.CMD_CHANGE_CHANNEL_STATE.getBytes(), params);
        //raw.sendCmd(Command.CMD_CHANGE_CHANNEL_STATE, params);
    }

    @Override
    public String setCountry(int code) {
        return WriteAndReadComm(Command.CMD_SET_COUNTRY.getBytes(), new String[]{code + ""});
        //raw.sendCmd(Command.CMD_SET_COUNTRY, new String[]{code+""});
    }

    @Override
    public String getCountryCapability() {
        return WriteAndReadComm(Command.CMD_GET_COUNTRY_CAPABILITY.getBytes(), null);
        //raw.sendCmd(Command.CMD_GET_COUNTRY_CAPABILITY, null);
    }

    @SuppressWarnings("null")
    @Override
    public Message readRagMemory(int w_count, int mem, int w_offset,
                                 long acs_pwd) {

        if (w_count <= 0) {
            message.setCode(1);
            message.setMessage("w_count not <= 0");
            message.setResult(null);
            return message;
        }

        if (mem < 0 || mem > 3) {
            message.setCode(1);
            message.setMessage("Memory is error");
            message.setResult(null);
            return message;
        }

        if (w_offset < 0) {
            message.setCode(1);
            message.setMessage("w_offset not < 0");
            message.setResult(null);
            return message;
        }
        String[] params = {
                w_count + "",
                mem + "",
                w_offset + "",
                acs_pwd + "",
                f_s + "",
                f_m + "",
                to + ""
        };
        String result = WriteAndReadComm(Command.CMD_READ_RAG_MEMORY.getBytes(), params);
        if (result != null && !result.contains("err") && result.startsWith("ok") && result.contains("e=" + strEpc)) {
            message.setCode(0);
            message.setMessage("Read Tag Successful!");
            message.setResult(result);
            return message;
        } else {
            message.setCode(1);
            message.setMessage("Read Tag Failure!");
            message.setResult(result);
            return message;
        }

        //raw.sendCmd(Command.CMD_READ_RAG_MEMORY, params);
    }

    @Override
    public Message writeTagMemory(int w_count, int mem, int w_offset, String data,
                                  long acs_pwd) {

        if (w_count <= 0) {
            message.setCode(1);
            message.setMessage("w_count not <=0!");
            message.setResult(null);
            return message;
        }

        if (mem < 0 || mem > 3) {
            message.setCode(1);
            message.setMessage("Write Tag Failure!");
            message.setResult(null);
            return message;
        }

        if (w_offset < 0) {
            message.setCode(1);
            message.setMessage("Write Tag Failure!");
            message.setResult(null);
            return message;
        }

        if (StringUtils.isBlank(data)) {
            message.setCode(1);
            message.setMessage("Data not can empty!");
            message.setResult(null);
            return message;
        }

        if (data.length() != w_count * 4) {
            message.setCode(1);
            message.setMessage("Write Tag Failure!");
            message.setResult(null);
            return message;
        }

        String[] params = {
                w_count + "",
                mem + "",
                w_offset + "",
                data,
                acs_pwd + "",
                f_s + "",
                f_m + "",
                to + ""
        };

        String result = WriteAndReadComm(Command.CMD_WRITE_TAG_MEMORY.getBytes(), params);
        if (result != null && !result.contains("err") && result.startsWith("ok") && result.contains("e=" + strEpc)) {
            message.setCode(0);
            message.setMessage("Write Tag Successful!");
            message.setResult(result);
            return message;
        } else {
            message.setCode(1);
            message.setMessage("Write Tag Failure!");
            message.setResult(result);
            return message;
        }
        //raw.sendCmd(Command.CMD_WRITE_TAG_MEMORY, params);
        //return "";
    }

    @Override
    public boolean killTag(long kill_pwd) {
        String[] params = {
                kill_pwd + "",
                f_s + "",
                f_m + "",
                to + ""
        };
        String result = WriteAndReadComm(Command.CMD_KILL_TAG.getBytes(), params);
        return result != null && !result.contains("err")
                && result.trim().startsWith("ok") && result.contains("e=" + strEpc);
        //raw.sendCmd(Command.CMD_KILL_TAG, params);
    }

    @Override
    public boolean lockTagMemory(int user, int tid, int epc, int acs_pwd,
                                 int kill_pwd, long ACS_PWD) {

        String[] params = {user == -1 ? "" : (user + ""),
                tid == -1 ? "" : (tid + ""), epc == -1 ? "" : (epc + ""),
                acs_pwd == -1 ? "" : (acs_pwd + ""),
                kill_pwd == -1 ? "" : (kill_pwd + ""), ACS_PWD + "", f_s + "",
                f_m + "", to + ""};
        String result = WriteAndReadComm(Command.CMD_LOCK_TAG_MEMORY.getBytes(), params);
        return result != null && !result.contains("err")
                && result.startsWith("ok") && result.contains("e=" + strEpc);
        //raw.sendCmd(Command.CMD_LOCK_TAG_MEMORY, params);
    }

    @Override
    public void setLockStatePerm(int mem_id, int f_l, int ACS_PWD, int f_s,
                                 int f_m, int to) {
        String[] params = {
                mem_id + "",
                f_l + "",
                ACS_PWD + "",
                f_s + "",
                f_m + "",
                to + ""
        };
        WriteAndReadComm(Command.CMD_SET_LOCK_STATE_PERM.getBytes(), params);
        //raw.sendCmd(Command.CMD_SET_LOCK_STATE_PERM, params);
    }

    @Override
    public void pauseTX() {
        WriteAndReadComm(Command.CMD_PAUSE_TX.getBytes(), null);
        //raw.sendCmd(Command.CMD_PAUSE_TX, null);
    }

    @Override
    public void heartBeat(int value) {
        WriteAndReadComm(Command.CMD_HEART_BEAT.getBytes(), new String[]{value + ""});
        //raw.sendCmd(Command.CMD_HEART_BEAT, new String[]{value+""});
    }

    @Override
    public void statusReport(int f_link) {
        WriteAndReadComm(Command.CMD_STATUS_REPORT.getBytes(), new String[]{f_link + ""});
        //raw.sendCmd(Command.CMD_STATUS_REPORT, new String[]{f_link+""});
    }

    @Override
    public void setInventoryReportFormat(int f_time, int f_rssi) {
        WriteAndReadComm(Command.CMD_INVENTORY_REPORT_FORMAT.getBytes(), new String[]{f_time + "", f_rssi + ""});
        //raw.sendCmd(Command.CMD_INVENTORY_REPORT_FORMAT, new String[]{f_time+"",f_rssi+""});
    }

    @Override
    public void setSystemTime(long val) {
        WriteAndReadComm(Command.CMD_SYSTEM_TIME.getBytes(), new String[]{val + ""});
        //raw.sendCmd(Command.CMD_SYSTEM_TIME, new String[]{val+""});
    }

    @Override
    public void disLink() {
        WriteAndReadComm(Command.CMD_DISLINK.getBytes(), null);
        //raw.sendCmd(Command.CMD_DISLINK, null);
    }


    /**
     * @param data
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public synchronized String WriteAndReadComm(byte[] data, String[] params) {
        PacketParser packetParser = null;
        Map map = null;
        //byte[] recvData = new byte[1024];
        try {
            if (params != null) {
                raw.sendCmd(new String(data), params);
            } else {
                //raw.sendCmd(data);
                raw.sendCmd_rn(data);
            }
            packetParser = new PacketParser();
            map = packetParser.analysisData(raw.recvData());
            //recvData = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String msg = (String) map.get("msg");
        if (msg != null)
            return msg;
        else
            return "空";
    }
}
