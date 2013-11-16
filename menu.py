import gobject
import gtk
import appindicator
import sys
import os

def quitApplication(w, optionName):
    sys.exit(0)
 
def config(w,optionName):
    os.popen('mvn exec:java -Dexec.mainClass="io.loli.sc.ConfigFrame"')
    
def fullScreenShot(w, optionName):
    os.popen('mvn exec:java -Dexec.mainClass="io.loli.sc.ScreenCaptor"')
    
def selectScreenShot(w,optionName):
    os.popen('mvn exec:java -Dexec.mainClass="io.loli.sc.DragFrame"')
    
if __name__ == "__main__":
  ind = appindicator.Indicator ("sc-java",
                              "indicator-messages",
                              appindicator.CATEGORY_APPLICATION_STATUS)
  ind.set_status (appindicator.STATUS_ACTIVE)
  ind.set_attention_icon ("indicator-messages-new")
 
  # create a menu
  menu = gtk.Menu()
  
  
  fsmenun = "Full ScreenShot"
  fsmenu = gtk.MenuItem(fsmenun)
  fsmenu.connect("activate",fullScreenShot, fsmenun)
  menu.append(fsmenu)
 
  ssmenun = "Select ScreenShot"
  ssmenu = gtk.MenuItem(ssmenun)
  ssmenu.connect("activate",selectScreenShot, ssmenun)
  menu.append(ssmenu)
  
  opmenun = "Option"
  opmenu = gtk.MenuItem(opmenun)
  opmenu.connect("activate",config, opmenun)
  menu.append(opmenu)
  
  qmenun = "Quit"
  qmenu = gtk.MenuItem(qmenun)
  menu.append(qmenu)
  qmenu.connect("activate", quitApplication, qmenun)
  
  fsmenu.show()
  ssmenu.show()
  opmenu.show()
  qmenu.show()
  ind.set_menu(menu)
 
  gtk.main()