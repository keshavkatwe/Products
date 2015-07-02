<?php

include_once './config/Database.php';

$database = new Database();
$conn = $database->get_connection();

$_SERVER['CONTENT_TYPE'] = "application/x-www-form-urlencoded";


$content = json_decode(file_get_contents('php://input'), TRUE);




if ($_GET['type'] == "add") {
    $user_id = $content['userid'];
    $product_id = $content['productid'];
    $product_type = $content['producttype'];


    if ($conn->query("insert into user_products (user_id, product_id, product_type) values ($user_id, '$product_id', '$product_type')")) {
        $response = array(
            'success' => TRUE,
        );
    } else {
        $response = array(
            'success' => FALSE,
        );
    }

    echo json_encode($response);
} else if ($_GET['type'] == "get") {
    
}

