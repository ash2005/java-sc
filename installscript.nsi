;NSIS Modern User Interface
;Written by Joost Verburg

;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"

;--------------------------------
;General
 
  !define MUI_ICON "C:\Users\ちよこ\Desktop\output\icon.ico"
 ;Name and file
  Name "云图"
  OutFile "screen-installer.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\SCREEN.PICS\screen"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\SCREEN.PICS" ""

  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_FINISHPAGE_RUN
  !define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink" 
  ;function
  Function LaunchLink
     Exec "$INSTDIR\screenpics.exe"
  FunctionEnd

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "C:\Users\ちよこ\Desktop\output\LICENCE.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  Page custom CheckBoxPage
  !insertmacro MUI_PAGE_FINISH
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "SimpChinese"

;--------------------------------
;Installer Sections

Section "程序核心组件" SecDummy

  SetOutPath "$INSTDIR"
  
  ;Put file there
  SetOutPath $INSTDIR
  File C:\Users\ちよこ\Desktop\output\*
  SetOutPath $INSTDIR\system_lib
  File C:\Users\ちよこ\Desktop\output\system_lib\*
  SetOutPath $INSTDIR\io.loli.sc_lib
  File C:\Users\ちよこ\Desktop\output\io.loli.sc_lib\*  

  ;Store installation folder
  WriteRegStr HKCU "Software\SCREEN.PICS" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  ; 为 Windows 卸载程序写入键值
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SCREEN.PICS" "DisplayName" "SCREEN.PICS（只用于移除）"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SCREEN.PICS" "UninstallString" "$INSTDIR\uninst.exe"

  ;Register dll
  !include "x64.nsh"
  ${If} ${RunningX64}
   System::Call "Kernel32::Wow64EnableWow64FsRedirection(i 0)"
   CopyFiles "$INSTDIR\system_lib\JIntellitype64.dll" $SYSDIR\JIntellitype.dll
  ${Else}    
   nsExec::ExecToLog 'cmd.exe /c copy "$INSTDIR\system_lib\JIntellitype64.dll" $SYSDIR\JIntellitype.dll'
  ${EndIf}
  

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "程序核心功能"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ;ADD YOUR OWN FILES HERE...
  Delete "$DESKTOP\screenpics.lnk"
  Delete "$QUICKLAUNCH\screenpics.lnk"
  Delete "$SMPROGRAMS\screenpics.lnk"
  Delete "$INSTDIR\Uninstall.exe"
  Delete "$INSTDIR\*"
  Delete "$INSTDIR\io.loli.sc_lib\*"
  Delete "$INSTDIR\system_lib\*"
  RMDir "$INSTDIR\io.loli.sc_lib"
  RMDir "$INSTDIR\system_lib"
  RMDir "$INSTDIR"

  DeleteRegKey /ifempty HKCU "Software\SCREEN.PICS"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Run\SCREEN"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SCREEN.PICS" 
SectionEnd


Function .onInit
InitPluginsDir
;如果要做多语言界面，可以在这里根据具体操作系统语言来更改ini的文件名
File /oname=$PLUGINSDIR\checkbox.ini checkbox.ini
FunctionEnd



Function CheckBoxPage
    InstallOptions::dialog "$PLUGINSDIR\\checkbox.ini"
FunctionEnd

;最后一页的checkbox
Function .onInstSuccess
ReadINIStr $R0 "$PLUGINSDIR\\checkbox.ini" "Field 3" "State"
ReadINIStr $R1 "$PLUGINSDIR\\checkbox.ini" "Field 4" "State"
ReadINIStr $R2 "$PLUGINSDIR\\checkbox.ini" "Field 5" "State"
IntCmp $R0 1 0 +2 +2
    CreateShortCut "$DESKTOP\screenpics.lnk" "$INSTDIR\screenpics.exe" "" "$INSTDIR\screenpics.exe" 0
IntCmp $R1 1 0 +2 +2
    CreateShortCut "$SMPROGRAMS\screenpics.lnk" "$INSTDIR\screenpics.exe" "" "$INSTDIR\screenpics.exe" 0
IntCmp $R2 1 0 +2 +2
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Run" "SCREEN" "$INSTDIR\screenpics.exe"
FunctionEnd