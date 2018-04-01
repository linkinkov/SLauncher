<?php
function db() {
    $class = 'MongoClient'; 
    if(!class_exists($class)){      
    $class = 'Mongo';    
    }
    try {
        $mongo = new $class('skins.angryworld.net');
        $db = $mongo->selectDB("Database");
    } catch (Exception $e) {
        exit("Database connection failed");
    }
    return $db;
}

?>