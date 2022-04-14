#!/bin/bash

for file in *; do
  echo -e -n "\e[0;34mTESTING FILE $file: \e[0;30m" #\033[0m blue: \e[0;34m
  if [[ $(grep 'oracle' $file) = *pass* ]]; then
    if [[ $(diff ../oracle/pass_output <(java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck <"$file" 2> ../error)) = *---* ]]; then
      echo -e '\e[0;31mFAIL\e[0;30m'
    else
      echo -e '\e[0;32mPASS\e[0;30m'
    fi
  else
    if [[ $(diff ../oracle/fail_output <(java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck <"$file" 2> ../error)) = *---* ]]; then
      echo -e '\e[0;31mFAIL\e[0;30m'
      cat ../error
#      echo -n > ../error
    else
      echo -e '\e[0;32mPASS\e[0;30m'
    fi
  fi
  #echo
done

#for file in *
#do
#  #echo $file
#  echo testing $file
#  if [[ $(grep 'oracle' $file) = *pass* ]]; then
#    #echo found pass
#    diff ../oracle/pass_output <(java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck < $file)
#    #echo done
#  else
#    #echo found fail
#    diff ../oracle/fail_output <(java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck < $file) #time
#  fi
#  #java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck < $file #time
#  echo
#done
##find -name "*.txt" -exec java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck {} \;
##echo 'Should fail'
##java -cp "/home/nick/scpdir/Typecheck/target/classes" Typecheck < cases/CaseX.txt
