#! /bin/bash
rm release/gam*jar
cp dist/*.jar release/ 
mv release/combuddhagame.jar release/game.jar
cp -R /home/andreas/character release/
cp release/game*.jar builds/
if [ "$1" == "-test" ]; then
cd release;
java -jar game-*.jar;
fi
if [ "$1" == "-git" ]; then
git add -A ./;
git commit -m ${@:2};
git push -u game source;
cd release;
git add -A ./;
git commit -m ${@:2};
git push -u game master;
fi
