<?php

class Database {

    private $host;
    private $username;
    private $password;
    private $database;

    function __construct() {
        $this->host = "localhost";
        $this->username = "root";
        $this->password = "";
        $this->database = "product";
    }

    function get_connection() {
        // Create connection
        $conn = new mysqli($this->host, $this->username, $this->password, $this->database);

// Check connection
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        return $conn;
    }

}
