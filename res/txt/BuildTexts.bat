
chcp 65001

ECHO en.lng> lng.txt
ECHO English>> lng.txt
ECHO Select language>> lng.txt
ECHO es.lng>> lng.txt
ECHO Español>> lng.txt
ECHO Seleccionar idioma>> lng.txt
ECHO fr.lng>> lng.txt
ECHO Français>> lng.txt
ECHO Sélectionnez langue>> lng.txt
ECHO pt-BR.lng>> lng.txt
ECHO Brasileiro>> lng.txt
ECHO Selecionar o idioma>> lng.txt
ECHO ru.lng>> lng.txt
ECHO русский>> lng.txt
ECHO Выберите язык>> lng.txt
ECHO ar.lng>> lng.txt
ECHO العربية>> lng.txt
ECHO اختر اللغة>> lng.txt
ECHO bn.lng>> lng.txt
ECHO বাংলা>> lng.txt
ECHO ভাষা নির্বাচন>> lng.txt
ECHO ja.lng>> lng.txt
ECHO 日本語>> lng.txt
ECHO|set /p=言語を選ぶ>> lng.txt

TextExporter2    cf.lng     lng.txt
move cf.lng EFIGS
del lng.txt

ECHO en.lng> lng.txt
ECHO English>> lng.txt
ECHO Select language>> lng.txt
ECHO es.lng>> lng.txt
ECHO Español>> lng.txt
ECHO Seleccionar idioma>> lng.txt
ECHO pt-BR.lng>> lng.txt
ECHO Brasileiro>> lng.txt
ECHO Selecionar o idioma>> lng.txt
ECHO ar.lng>> lng.txt
ECHO العربية>> lng.txt
ECHO اختر اللغة>> lng.txt
ECHO bn.lng>> lng.txt
ECHO বাংলা>> lng.txt
ECHO|set /p=ভাষা নির্বাচন>> lng.txt

TextExporter2    cf.lng     lng.txt
move cf.lng ESPAB
del lng.txt

ECHO en.lng>lng.txt
ECHO English>> lng.txt
ECHO Select language>> lng.txt
ECHO es.lng>> lng.txt
ECHO Español>> lng.txt
ECHO|set /p=Seleccionar idioma>> lng.txt

TextExporter2    cf.lng     lng.txt
move cf.lng ENGSP
del lng.txt

@ECHO OFF

rem    ## Pack all language files into a big resource file

TextExporter2    en.lng     EN_game.txt
TextExporter2    es.lng     SP_game.txt
TextExporter2    fr.lng     FR_game.txt
TextExporter2    pt-BR.lng  BR_game.txt
TextExporter2    bn.lng     BN_game.txt
TextExporter2    ru.lng     RU_game.txt
TextExporter2    ar.lng     AR_game.txt
TextExporter2    ja.lng     JP_game.txt

copy en.lng ENGSP
copy es.lng ENGSP
copy en.lng ESPAB
copy es.lng ESPAB
copy pt-BR.lng ESPAB
copy ar.lng ESPAB
copy bn.lng ESPAB

move en.lng EFIGS
move es.lng EFIGS
move fr.lng EFIGS
move it.lng EFIGS
move pt.lng EFIGS
move pt-BR.lng EFIGS
move ru.lng EFIGS
move bn.lng EFIGS
move ar.lng EFIGS
move ja.lng EFIGS


pause