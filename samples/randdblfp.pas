program randdblfp;
{$mode objfpc}

procedure testNextDouble;
var
  i : integer;
  f : double;
begin
  randseed := 1234567890;
  for i:=1 to 20 do
  begin
    f := random;
    writeln(f: 17 : 16);
  end;
end;

begin
  testNextDouble;
end.
