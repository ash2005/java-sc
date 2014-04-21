package io.loli.sc.system;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class H {
    public static void main(String[] args) throws InterruptedException{
        Provider provider = Provider.getCurrentProvider(true);
        provider.register(KeyStroke.getKeyStroke(KeyEvent.VK_1,KeyEvent.CTRL_MASK), new HotKeyListener(){

            @Override
            public void onHotKey(HotKey arg0) {
                System.out.println(arg0.keyStroke.getKeyCode()==KeyEvent.VK_1);
            }
            
        });
    }
}
