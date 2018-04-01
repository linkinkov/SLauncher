<?php
include("/slauncher_stats/internal/Database.php");

date_default_timezone_set('Europe/Moscow');
use \Psr\Http\Message\ServerRequestInterface as Request;
$now = strtotime("now");
$date = date("Y-m-d", $yesterday);
$time = date("H:i:s");

if (isset($_GET['action'])) {
	if ($_GET['action'] == 'mc_launched') {
		if (!isset($_GET['mc_version']) || !isset($_GET['launcher_os']) || !isset($_GET['version'])) {
			return;
		}
		$mc_version = $_GET['mc_version'];
		$launcher_os = $_GET['launcher_os'];
		$version = $_GET['version'];
		mc_launches()->insert(array("time" => $time,"version" => $version, "mc_version" => $mc_version, "launcher_os" => $launcher_os, "date" => $date, "ip" => ""));
	}
}

function mc_launches()
{
    $db = db();
    return $db->mc_launches;
} 