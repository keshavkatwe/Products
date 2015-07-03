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
    $udate = $content['udate'];


    if ($conn->query("insert into user_products (user_id, product_id, product_type, udate) values ($user_id, '$product_id', '$product_type', '$udate')")) {
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
    $sql = "SELECT * from user_products where user_id=" . $_GET['user_id'] . " order by uproduct_id desc";
    $result = $conn->query($sql);

    $products_array = array();

    while ($row = $result->fetch_assoc()) {
        $row['is_available'] = is_available($row['product_id'], $conn);
        $products_array[] = $row;
    }

    echo json_encode($products_array);
} 
else if($_GET['type'] == "edit")
{
	$uproduct_id = $_GET['uproduct_id'];
	$user_id = $content['userid'];
    $product_id = $content['productid'];
    $product_type = $content['producttype'];
    $udate = $content['udate'];


    if ($conn->query("update user_products set product_id = '$product_id', product_type = '$product_type', udate = '$udate' where uproduct_id=$uproduct_id")) {
        $response = array(
            'success' => TRUE,
        );
    } else {
        $response = array(
            'success' => FALSE,
        );
    }

    echo json_encode($response);
}
else if($_GET['type'] == "delete")
{
	$uproduct_id = $_GET['uproduct_id'];
	
	if ($conn->query("delete from user_products where uproduct_id=$uproduct_id")) {
        $response = array(
            'success' => TRUE,
        );
    } else {
        $response = array(
            'success' => FALSE,
        );
    }

    echo json_encode($response);
	
	
}

function is_available($product_id, $conn) {
    $result = $conn->query("SELECT * from products where product_id='$product_id'");
    if ($result->num_rows > 0) {
        return TRUE;
    } else {
        return FALSE;
    }
}
