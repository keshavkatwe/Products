<?php

$fp = fopen("log/".date("d M Y").'.txt', "wb");
fwrite($fp, $content);
fclose($fp);