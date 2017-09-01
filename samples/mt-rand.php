<?php
// https://github.com/php/php-src/blob/master/ext/standard/mt_rand.c#L217
echo mt_getrandmax(), "\n";
mt_srand(1234567890, MT_RAND_MT19937);
for ($i = 0; $i < 10; $i++) {
    echo mt_rand(0, 16), "\n";
}
?>