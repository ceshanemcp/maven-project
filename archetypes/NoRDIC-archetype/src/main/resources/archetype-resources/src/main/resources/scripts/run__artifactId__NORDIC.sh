#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#!/usr/bin/ksh
# -----------------------------------------------------------------------------
# Module Name: run${artifactId}NORDIC.sh 
# Module Type: UNIX/Linux
# Description: Appworx script to process execute ${artifactId} NORDIC 
#
# Exit Codes:       0 -- Successful
#                   1 -- General Error
#                   
# Parameters:
# Called by:   Appworx job - 
#
# Modification
# Date         Changed By        Description
# ------------ ----------------- ----------------------------------------------
# 01/19/2015   Charles E. Shane  Initial Creation in NoRDIC Archetype
# ${date}      ${devName}        Initial Creation via NoRDIC Archetype
# -----------------------------------------------------------------------------
## Functions
function catLogFile {
  tail -300 $LOG |
      awk '{
          line = $0
          print "   >  " line
      }'
}

## Start script
BASE=/app01/devry/lib/${artifactId}NoRDIC/
cd $BASE

LOG=$BASE/logs/${artifactId}NoRDIC.log

echo "${artifactId} NoRDIC Process starting at - `date`" >> $LOG
RetVal="`/app01/java/jdk1.8.0_92/java -cp ${artifactId}.jar:conf:lib -Xms128m -Xmx512m -Dlog4j.configuration=log4j.properties com.devry.integration.nordic.framework.driver.NoRDICExecutor ${artifactId}Context.xml`"

EXITSTATUS=$?

echo $RetVal

# Check to see if process was completed successfully.
if [ $EXITSTATUS -eq 0 ]; then
     echo "${artifactId} NoRDIC Process completed - `date`" >> $LOG
     echo "EXITSTATUS:  " $EXITSTATUS >> $LOG
     exit $EXITSTATUS
else
    catLogFile
	 
    echo "${artifactId} NoRDIC - an error occurred during processing." >> $LOG
    echo "EXITSTATUS:  " $EXITSTATUS >> $LOG
		 
	exit $EXITSTATUS
fi