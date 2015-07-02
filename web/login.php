<?php

include_once './config/Database.php';

$database = new Database();
$conn = $database->get_connection();

$_SERVER['CONTENT_TYPE'] = "application/x-www-form-urlencoded";

$content = json_decode(file_get_contents('php://input'), TRUE);


$email = $content['email'];
$password = $content['password'];
$user_exist = FALSE;
$name = "";
$id = "";



$result = $conn->query("select * from users where email='$email' and password='$password'");

if($result->num_rows > 0)
{
    $user_exist = TRUE;
    $row = $result->fetch_assoc();
    $name = $row['name'];
    $id = $row['user_id'];
}

$response = array(
    'user_exist' => $user_exist,
    'name' => $name,
    'id' => $id
);

echo json_encode($response);
