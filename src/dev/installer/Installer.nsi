;--------------------------------
; INCLUDES
	!include "MUI2.nsh"
	;!include "nsProcess.nsh"
	!include "Sections.nsh"
	!include "FileFunc.nsh"
	
;--------------------------------
; VARIABLES
	!define VERSION "1.0"
	Var PATH
	InstallDir "$PROGRAMFILES\WebDL"
	InstallDirRegKey HKLM SOFTWARE\WebDL "Path"

;--------------------------------
; GENERAL CONFIG
	;SilentInstall silent
	SetOverwrite on
	SetDatablockOptimize on
	SetCompressor lzma
	SetCompress auto
	CRCCheck on
	XPStyle on
	AutoCloseWindow false
	ShowInstDetails show
	
	OutFile WebDL_${VERSION}.exe
	Name "WebDL"
	Caption "WebDL protocol"
	Icon icon.ico
	
;--------------------------------
; INTERFACE
	!define MUI_ICON icon.ico
	!define MUI_UNICON uninstall.ico
	!define MUI_ABORTWARNING
	!define MUI_UNABORTWARNING

;--------------------------------
; INSTALLER PAGES
  
	!insertmacro MUI_PAGE_WELCOME
	!insertmacro MUI_PAGE_LICENSE "license.txt"
	!insertmacro MUI_PAGE_DIRECTORY
	!insertmacro MUI_PAGE_INSTFILES
	!insertmacro MUI_PAGE_FINISH
  
; UNINSTALLER PAGES
	!insertmacro MUI_UNPAGE_WELCOME
	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
	!insertmacro MUI_UNPAGE_FINISH
	
;--------------------------------
; LANGUAGE
	!insertmacro MUI_LANGUAGE "English"

;--------------------------------
; UNINSTALL MACRO

!define CHECK_PATH "\WebDL"
!macro uninstall un
Function ${un}uninstall
    
    StrLen $R1 "${CHECK_PATH}"
    StrCpy $R0 $INSTDIR "" -$R1
    StrCmp $R0 "${CHECK_PATH}" +2
      Abort
     
    IfFileExists "$INSTDIR\*.*" 0 +2
    IfFileExists "$INSTDIR\WebDL.exe" +2
      Abort

    StrCpy $PATH "WebDL"
    
    SetShellVarContext all

    RMDir /r $INSTDIR\$PATH
    RMDir /r $INSTDIR 
    RMDir /r $SMPROGRAMS\$PATH

    DeleteRegKey HKLM "SOFTWARE\$PATH"
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \"
    
	DeleteRegKey HKEY_CLASSES_ROOT "webdl"
FunctionEnd
!macroend

!insertmacro UNINSTALL ""
!insertmacro UNINSTALL "un."


;--------------------------------
; INSTALLER SECTIONS

Section "Program"

	;If the installer is called with / u, it's uninstalled 
	Call IsUninstall
	Pop $0
	StrCmp $0 1 0 +3
		Call uninstall
		Abort

	StrCpy $PATH "WebDL"
	SetOutPath $INSTDIR

	;Include program files
	File  WebDL.exe
	File  license.txt
	File /r java32\jre
	File  icon.ico
	File application.properties
    
	;Affect to all users
	SetShellVarContext all

	;Menu items
	CreateDirectory "$SMPROGRAMS\WebDL"
	CreateShortCut "$SMPROGRAMS\WebDL\WebDL.lnk" "$INSTDIR\WebDL.exe"
	CreateShortCut "$SMPROGRAMS\WebDL\Uninstall.lnk" "$INSTDIR\Uninstall.exe"

	;Add entry in "Program and Features"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "DisplayName" "WebDL"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "DisplayIcon" "$INSTDIR\icon.ico"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "Publisher" "Deveuge"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "DisplayVersion" "${VERSION}"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "UninstallString" "$INSTDIR\Uninstall.exe"
    
	;Calculate the estimated size to add it to "Program and Features"
	${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
	IntFmt $0 "0x%08X" $0
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$PATH \" "EstimatedSize" "$0"
    
	WriteUninstaller "Uninstall.exe"

 	WriteRegStr HKLM SOFTWARE\$PATH "Path" $INSTDIR
	WriteRegStr HKLM SOFTWARE\$PATH "Version" "${VERSION}"
    
    ;Add keys to registry
    WriteRegStr HKEY_CLASSES_ROOT "webdl" "" "URL:webdl Protocol"
	WriteRegStr HKEY_CLASSES_ROOT "webdl\DefaultIcon" "" "$INSTDIR\icon.ico"
	WriteRegStr HKEY_CLASSES_ROOT "webdl" "URL Protocol" ""
	WriteRegStr HKEY_CLASSES_ROOT "webdl\shell\open\command" "" '$INSTDIR\WebDL.exe "%1"'

SectionEnd

;--------------------------------
; UNINSTALLER SECTIONS

Section "Uninstall"
	Call un.uninstall
SectionEnd

;--------------------------------
; AUX FUNCTIONS

;Function that checks if "/ u" is passed as argument to uninstall 
Function IsUninstall
		Push $0
		Push $CMDLINE
		Push "/u"
		Call StrStr
		Pop $0
		StrCpy $0 $0 3
		StrCmp $0 "/u" uninstall
		StrCmp $0 "/u " uninstall
		StrCpy $0 0
		Goto notuninstall
	uninstall: StrCpy $0 1
	notuninstall: Exch $0
FunctionEnd

;Function that gets the arguments 
Function StrStr
	Exch $R1 ; st=haystack,old$R1, $R1=needle
	Exch    ; st=old$R1,haystack
	Exch $R2 ; st=old$R1,old$R2, $R2=haystack
	Push $R3
	Push $R4
	Push $R5
	StrLen $R3 $R1
	StrCpy $R4 0
	; $R1=needle
	; $R2=haystack
	; $R3=len(needle)
	; $R4=cnt
	; $R5=tmp
	loop:
		StrCpy $R5 $R2 $R3 $R4
		StrCmp $R5 $R1 done
		StrCmp $R5 "" done
		IntOp $R4 $R4 + 1
		Goto loop
	done:
		StrCpy $R1 $R2 "" $R4
		Pop $R5
 		Pop $R4
		Pop $R3
		Pop $R2
		Exch $R1
FunctionEnd

