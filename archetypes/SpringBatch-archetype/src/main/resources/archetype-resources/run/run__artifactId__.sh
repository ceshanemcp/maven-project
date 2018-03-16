#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#!/usr/bin/ksh
# -----------------------------------------------------------------------------
# Module Name: run${artifactId}.sh
# Module Type: UNIX
# Description: This run script will start the ${artifactId} Spring batch process
#
# Parameters:  $1 = LOGGING
#              $2 = FromDateTime for RERUN FORMAT yyyy-MM-dd hh:mm:ss
#              $3 = ToDateTime for RERUN FORMAT yyyy-MM-dd hh:mm:ss
# Called by:   Appworx job - ${artifactId}
#
# Exit Codes:  0 - NO ERROR
#              1 - Uknown Error
#              2 - FAILED
#              3 - Parameters are invalid
#              11 - Serialization Error
#
# Modification
# Date         Changed By        Description
# ------------ ----------------- ----------------------------------------------
# ${date}   ${devName}   Initial Creation
# -----------------------------------------------------------------------------
cd /app01/devry/lib/${artifactId}
BASE=`pwd`

. /app01/devry/lib/${artifactId}/conf/process.properties

DTE1=`date +%m%d%Y`
NOW=`date +%m/%d/%Y-%H:%M`
DT_TSTAMP=`date +%m%d%Y-%H%M%S`
LOGS=$BASE/logs/$LOGFILE
JOB_RUN_COUNT=0
FAILURES=0
PROCESSED=0
LOG4J=$1
FROM_DATE_TIME=$2
TO_DATE_TIME=$3

# -------------------------------------------------------------------- #
# Functions                                                            #
# -------------------------------------------------------------------- #
run${artifactId}(){
    cd $BASE
    RetVal="`java -cp conf:${artifactId}.jar -Xms128m -Xmx512m $MAIN $LOG4J $FROM_DATE_TIME $TO_DATE_TIME`"
    #Check output
    EXITSTATUS=$?
}

# -------------------------------------------------------------------- #
# Start                                                                #
# -------------------------------------------------------------------- #
echo "Starting ${artifactId} Process $NOW" >> $LOGS

# In case appworx sends in an empty value for LOG4J
if [ "$LOG4J" == "" ]
    then
    LOG4J=INFO
fi

# Run Process
run${artifactId}

    #If job errors out with 11 - NOSQLSERIALIZE sleep and re-run job
while [ $EXITSTATUS -eq 11 ] && [ $JOB_RUN_COUNT -lt $JOB_RETRY ] 
do
     echo "WARN - EXITSTATUS: " $EXITSTATUS " - RE-RUNNING" >> $LOGS
     echo "sleeping " $JOB_SLEEP " seconds" >> $LOGS
     sleep $JOB_SLEEP
     run${artifactId}
     JOB_RUN_COUNT=`expr $JOB_RUN_COUNT + 1`
done

if [ $EXITSTATUS -eq 0 ]
then
    echo "Process Completed Successfully" >> $LOGS
    exit 0
else
     echo "Process Failed" >> $LOGS
     tail -100 $LOGS
     exit $EXITSTATUS
fi