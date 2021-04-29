package com.yoyling.tools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardUtil {
	public static String getClipboardText() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
	    if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	    	try{
	    		String string = (String) contents.getTransferData(DataFlavor.stringFlavor);
	    		return string;
	    	}catch (Exception ex){
	    		ex.printStackTrace();
	    	}
	    }
		return "";
	}
}
