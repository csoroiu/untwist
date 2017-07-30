//free pascal code for random is located at:
//https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
program randdblfp;

procedure testNextDouble;
var i:integer;
    f:double;
begin
randseed:=1234567890;
for i:=1 to 20 do
  begin
   f := random;
   writeln(f:17:16);
  end;
end;

begin
testNextDouble;
end.
