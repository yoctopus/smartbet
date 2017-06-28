<?php
define('HOST','localhost');
define('USER','baittech_smartb');
define('PASS','@Change2017');
define('DB','baittech_smartbet');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select * from results";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('ID'=>$row[0],
	'time'=>$row[1], 
'flag'=>$row[2],
'league'=>$row[3],
'team'=>$row[4],
'prediction'=>$row[5],
'datelong'=>date(strtotime($row[6]))
    
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
