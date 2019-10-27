{$N+,E-}
{$D-,L-,Y-}
program randInt;
{$APPTYPE CONSOLE}
{dcc32 -B -CC -U..\Lib randInt.pas}

procedure randInteger(seed : longint; range, count : integer);
var
  i : integer;
begin
  randseed := seed;
  for i:=1 to count do
  begin
    writeln(random(range), ' ', randseed);
  end;
end;

procedure checkerror(error : integer; argument : string);
begin
  if (error <> 0) then
  begin
    writeln('invalid argument ', argument);
    halt(2);
  end;
end;

var
  seed : longint;
  range, count : integer;
  error : integer;
begin
  if ((paramcount < 2) or (paramcount > 3)) then
  begin
    writeln('Invalid number of arguments.');
    writeln('Usage: randInt seed range - generates one int in the interval [0, range)');
    writeln('Usage: randInt seed range count - generates count ints in the interval [0, range)');
    halt(1);
  end;

  val(paramstr(1), seed, error);
  checkerror(error, paramstr(1));
  val(paramstr(2), range, error);
  checkerror(error, paramstr(2));

  count := 1;
  if (paramcount = 3) then
  begin
    val(paramstr(3), count, error);
    checkerror(error, paramstr(3));
  end;
  randInteger(seed, range, count);
end.