<?php

if(! isset( $argv[1] ) ) {

    echo "informe o idBtg\n\n"; exit;
}

$idBtg = $argv[1];

$conn = mysqli_connect("177.153.231.93","root","loca1020");

mysqli_select_db($conn, "btg_consolidated");

$query = "show tables from btg_consolidated where Tables_in_btg_consolidated like '%_". $idBtg ."_%' and Tables_in_btg_consolidated not like 'peal%' and Tables_in_btg_consolidated not like '%_email';";

$result = $conn->query($query);

while( $row = $result->fetch_assoc() ) {

    $query = "SELECT * FROM `".$row['Tables_in_btg_consolidated']."_email` LIMIT 1";

    $resultIsEmpty = $conn->query($query);

    if( !empty($resultIsEmpty) && $resultIsEmpty->num_rows > 0 ) {

        echo "A tabela ".$row['Tables_in_btg_consolidated']."_email não esta vazia\n\n"; exit;
    }

    $query = "CREATE TABLE IF NOT EXISTS `".$row['Tables_in_btg_consolidated']."_email` (
        `createdAt` text,
        `isRecommendation` int(11) NOT NULL,
        `isSent` int(11) NOT NULL,
        `percentage` double NOT NULL,
        `platformId` int(11) NOT NULL,
        `productId` text,
        `userId` text,
        `userSent` text
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8";

    $conn->query($query);

    $query = "INSERT INTO `".$row['Tables_in_btg_consolidated']."_email` (`createdAt`,`isRecommendation`,`isSent`,`percentage`,`platformId`,`productId`,`userId`,`userSent`) 
              SELECT CURRENT_TIMESTAMP AS `createdAt`, `isRecommendation` AS `isRecommendation`, `isSent` AS `isSent`, `percentage` AS `percentage`, 1 AS `platformId`, `productId` AS `productId`, `cookieBid` AS `userId`, `email` AS `userSent` FROM `".$row['Tables_in_btg_consolidated']."` ";

    $conn->query($query);
}

echo "OK\n\n";

$conn->close();
?>