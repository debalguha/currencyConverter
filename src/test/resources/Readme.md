Convert the given amounts from source currency to destination currency. 
If there is a numerical value against the matrix then use that number to directly convert the currency. 
If it states another currency name then you may have to convert to that currency before converting to the destination currency. 
There could be more than one such conversion required. 
For example, SGD to EUR needs to consider SGD to USD first and then as USD to EUR. 
You are given two input files – ref.csv and data.csv and you are expected to write a program that outputs a file (must be output.csv) which is identical to data.csv but with an additional column (ToAmount) at the end as shown below. 
State your assumptions clearly in the mail response. 
If the code doesn’t run or fail for other datasets or doesn’t consider all the instructions your attempt would be considered a failure.
Your program should detect cyclic dependencies in the reference data or incomplete information, report these in a sensible manner, and exit with a non-zero exit code.
The files are included in src/test/resources directory.
