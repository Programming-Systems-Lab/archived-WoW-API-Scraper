<?php
$dbstr = "mysql:host=barclay.cs.columbia.edu;dbname=wow";
$username = "mysqluser";
$password = "mysqlpassword";



$dbh = new PDO($dbstr,$username,$password);

if(count($argv) != 3)
{
	print "Usage: php slaveController.php start n\n";
	die();
}
$cmd = $argv[1];
$i =$argv[2];
$where = "";
if(intval($i) > 0)
	$where="SLAVEGROUP=".intval($i).";";
else
	$where="NAME='".addslashes($i)."';";
	if($cmd == "start")
	{
		$stmt = $dbh->query("select INET_NTOA(s.ip),s.NAME,s.REQUESTSTODAY,s.REQUESTSTODAYDATE,USERNAME,SLAVEGROUP,s.ip,s.path,s.java from `SLAVE` s WHERE $where");
		while($row = $stmt->fetch())
		{
			$ip = $row[0];
			$name = $row[1];
			$user=$row[4];
			print "Starting $name...\n";
			`ssh $user@$ip 'nohup {$row[8]} {$row[7]} > logs/$name.log 2>&1 &'`;
			$st2=$dbh->query("UPDATE SLAVE set LASTSTART=NOW() where IP=$row[6]");
		}
	}
	else if($cmd == "startStopped")
	{
		$stmt = $dbh->query("select INET_NTOA(s.ip),s.NAME,s.REQUESTSTODAY,s.REQUESTSTODAYDATE,USERNAME,SLAVEGROUP,s.ip,s.path,s.java from `SLAVE` s WHERE LASTUPDATED < DATE_ADD(NOW(),INTERVAL -30 MINUTE) AND SLAVEGROUP>0;");
		while($row = $stmt->fetch())
		{
			$ip = $row[0];
			$name = $row[1];
			$user=$row[4];
			print "Starting $name...\n";
			`ssh $user@$ip 'nohup {$row[8]} {$row[7]} > logs/$name.log 2>&1 &'`;
			$st2=$dbh->query("UPDATE SLAVE set LASTSTART=NOW() where IP=$row[6]");
		}

	}
	else if($cmd == "startUncap")
	{
		$stmt = $dbh->query("select INET_NTOA(s.ip),s.NAME,s.REQUESTSTODAY,s.REQUESTSTODAYDATE,USERNAME,SLAVEGROUP,s.ip from `SLAVE` s WHERE $where");
		while($row = $stmt->fetch())
		{
			$ip = $row[0];
			$name = $row[1];
			print "Starting $name...\n";
			`ssh jbell@$ip 'nohup java -jar wowGuildScraperUnlimited.jar > logs/$name.log 2>&1 &'`;
			$st2=$dbh->query("UPDATE SLAVE set LASTSTART=NOW() where IP=$row[6]");
		}
	}
	else if($cmd == "kill")
	{
		$stmt = $dbh->query("select INET_NTOA(s.ip),s.NAME,s.REQUESTSTODAY,s.REQUESTSTODAYDATE,USERNAME,SLAVEGROUP,s.ip from `SLAVE` s WHERE $where");
		while($row = $stmt->fetch())
		{
			$ip = $row[0];
			$name = $row[1];
			print "Forcing kill on $name...\n";
			`ssh jbell@$ip 'killall java'`;
			$st2=$dbh->query("UPDATE SLAVE set LASTSTART=NOW() where IP=$row[6]");
		}
	}
	else if($cmd == "stop")
	{
		$stmt = $dbh->query("UPDATE `SLAVE` SET KILL_REQ=1 WHERE $where");
	}
?>
