<?php

if(! isset( $argv[1] ) ) {

    echo "informe o idBtg\n\n"; exit;
}

$idBtg = $argv[1];

$conn = mysqli_connect("177.153.231.93","root","loca1020");

mysqli_select_db($conn, "btg_consolidated");



################ check homologated
$queryAccount = "SELECT * FROM master.users WHERE btgId = ".$idBtg." AND homologation = 1";
$resultAccount = $conn->query($queryAccount);
if( $resultAccount->num_rows < 1 ) {

    echo "Conta não encontrada\n\n"; exit;
}
$rowAccount = $resultAccount->fetch_assoc();




################ find rules with peal 
$queryRule = "SELECT * FROM btg_panel.users_rules WHERE id IN (
	SELECT pealFrom FROM btg_panel.users_rules WHERE pealFrom != 0 AND isDeleted = 0 AND status = 1 AND userId = ".$rowAccount['id']."
)";
$resultRule = $conn->query($queryRule);
if( $resultRule->num_rows < 1 ) {

    echo "Nenhum repique encontrado\n\n"; exit;
}




################ get tables consolidated of peal 
$dateInit = date('Y_m', strtotime('-1 month'));
$dateFinish = date('Y_m');
$total = 0;
while( $rowRule = $resultRule->fetch_assoc() ) {

    echo $queryTables = "SHOW TABLES FROM btg_consolidated WHERE ( Tables_in_btg_consolidated LIKE '%\_".$rowAccount['btgId']."\_".$rowRule['id']."\_".$dateInit."\_%' OR Tables_in_btg_consolidated LIKE '%\_".$rowAccount['btgId']."\_".$rowRule['id']."\_".$dateFinish."\_%' ) AND Tables_in_btg_consolidated NOT LIKE 'peal%' AND Tables_in_btg_consolidated NOT LIKE '%_email';";

    $resultTables = $conn->query($queryTables);
    
    $total = $total + $resultTables->num_rows;

    ################# create new table consolidated if not exists
    while( $row = $resultTables->fetch_assoc() ) {

        $query = "SELECT * FROM `".$row['Tables_in_btg_consolidated']."_email` LIMIT 1";
    
        $resultIsEmpty = $conn->query($query);
    
        if( !empty($resultIsEmpty) && $resultIsEmpty->num_rows > 0 ) {
    
            echo "A tabela ".$row['Tables_in_btg_consolidated']."_email não esta vazia\n\n"; continue;
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

        echo $row['Tables_in_btg_consolidated']."_email\n";
    }
}

echo "\n\nTOTAL: " . $total . "\n";

$conn->close();
?>