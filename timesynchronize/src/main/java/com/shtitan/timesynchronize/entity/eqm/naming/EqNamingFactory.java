package com.shtitan.timesynchronize.entity.eqm.naming;

import com.shtitan.timesynchronize.enums.DeviceType;
import com.shtitan.timesynchronize.enums.EqObjectType;

public class EqNamingFactory {
	 public static EqNaming createNeNaming(Long neId,DeviceType deviceType){
	        neId=neId*100000+deviceType.getValue();
	        EqNaming name = new EqNaming("", neId,EqObjectType.ne);
	        return name;
	    }
	    /**
	     * 创建代理网元EqNaming
	     * @param upLinkPort
	     * @param branchNo
	     * @return
	     */
	    public static EqNaming createSubordinateNeNaming(EqNaming upLinkPort,int branchNo,DeviceType deviceType){
	        EqObjectType eqObjectType=null;
	        if(deviceType.isProxied())
	            eqObjectType=EqObjectType.psne;
	        else
	            eqObjectType=EqObjectType.isne;
	        
	        Long subNeId=Long.valueOf(branchNo)*100000+deviceType.getValue();
	        
	        EqNaming ne = new EqNaming(upLinkPort.getName(), subNeId,eqObjectType,"");
	        return ne;
	    }

	    /**
	     * 创建没有Rack的槽位EqNaming
	     * 
	     * @param neNaming
	     * @param EqObjectType
	     * @param cardNo
	     * @return
	     */
	    public static EqNaming createNoRackSlotNaming(EqNaming neNaming, Integer slot) {
	        if (!neNaming.isNe())
	            throw new IllegalArgumentException();
	        EqNaming reckNaming = createRackNaming(neNaming, 0);// 如果没有rack默认为0
	        EqNaming shelfNaming = createShelfNaming(reckNaming, 1);// 机框默认为1
	        EqNaming slotNaming = createSlotNaming(shelfNaming, slot);
	        return slotNaming;
	    }

	    /**
	     * 创建端口EqNaming
	     * 
	     * @param neId
	     *            网元Id
	     * @param slot
	     *            槽位号
	     * @param port
	     *            端口号
	     * @return
	     */
	    public static EqNaming createPortNaming(EqNaming neNaming, Integer slot, Integer port) {
	        EqNaming slotNaming = createNoRackSlotNaming(neNaming, slot);
	        return new EqNaming(slotNaming.getName(), Long.valueOf(port), EqObjectType.port);

	    }

	    /**
	     * 创建槽位EqNaming
	     * 
	     * @param shelfNaming
	     * @param slotNo
	     * @return
	     */
	    public static EqNaming createSlotNaming(EqNaming shelfNaming, Integer slot) {
	        if (!(shelfNaming.getEqObjectType() == EqObjectType.shelf))
	            throw new IllegalArgumentException();
	        return new EqNaming(shelfNaming.getName(), Long.valueOf(slot), EqObjectType.slot);
	    }

	    /**
	     * 创建端口 EqNaming
	     * 
	     * @return
	     */
	    public static EqNaming createPortNaming(EqNaming slotNaming, Integer port) {
	        if (slotNaming.getEqObjectType() != EqObjectType.slot)
	            throw new IllegalArgumentException();
	        return new EqNaming(slotNaming.getName(), Long.valueOf(port), EqObjectType.port);
	    }

	    /**
	     * 创建ADSL APVC EqNaming
	     * 
	     * @param portNaming
	     *            PVC所在的ADSL端口
	     * @param pvcIndex
	     *            PVC编号
	     * @return
	     */
	    public static EqNaming createPvcNaming(EqNaming portNaming, Integer pvcIndex) {
	        if (portNaming.getEqObjectType() != EqObjectType.port)
	            throw new IllegalArgumentException();
	        return new EqNaming(portNaming.getName(), Long.valueOf(pvcIndex), EqObjectType.pvc);
	    }

	    /**
	     * 创建ADSL APVC EqNaming
	     * 
	     * @param neId
	     *            网元Id
	     * @param slot
	     *            槽位号
	     * @param port
	     *            端口号
	     * @param pvcNo
	     *            PVC编号
	     * @return
	     */
	    public static EqNaming createPvcNaming(EqNaming ne, Integer slot, Integer port, Integer pvcNo) {
	        EqNaming portNaming = createPortNaming(ne, slot, port);
	        return new EqNaming(portNaming.getName(), Long.valueOf(pvcNo), EqObjectType.pvc);
	    }

	    /**
	     * 创建rack EqNaming
	     * 
	     * @param neDn
	     * @param rackNo
	     * @return
	     */
	    public static EqNaming createRackNaming(EqNaming neNaming, Integer rackNo) {
	        if (!neNaming.isNe())
	            throw new IllegalArgumentException();
	        return new EqNaming(neNaming.getName(), Long.valueOf(rackNo), EqObjectType.rack);

	    }

	    /**
	     * 创建shelf EqNaming
	     * 
	     * @param rackNaming
	     * @param shelf
	     * @return
	     */
	    public static EqNaming createShelfNaming(EqNaming rackNaming, Integer shelf) {
	        if (!(rackNaming.getEqObjectType() == EqObjectType.rack))
	            throw new IllegalArgumentException();
	        return new EqNaming(rackNaming.getName(), Long.valueOf(shelf), EqObjectType.shelf);
	    }

	    /**
	     * 得到EqNaming的槽道号
	     * 
	     * @param naming
	     * @return
	     */
	    public static int getSlotNoByNaming(EqNaming naming) {
	        return naming.getEqNamingByEqObjectType(EqObjectType.slot).getInstance().intValue();
	    }

	    /**
	     * 得到EqNaming的端口号
	     * 
	     * @param naming
	     * @return
	     */
	    public static int getPortNoByNaming(EqNaming naming) {
	        return naming.getEqNamingByEqObjectType(EqObjectType.port).getInstance().intValue();
	    }

	    /**
	     * 得到EqNaming的PVC号
	     * 
	     * @param naming
	     * @return
	     */
	    public static int getPvcNoByNaming(EqNaming naming) {
	        return naming.getEqNamingByEqObjectType(EqObjectType.pvc).getInstance().intValue();
	    }
	    
	    
	
	    
}
