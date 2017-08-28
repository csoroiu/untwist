{$N+,E-}
{$D-,L-,Y-}
program randInt;
{$APPTYPE CONSOLE}
{dcc32 -B -CC -U..\Lib randInt.pas}

procedure randIntSingle(seed : longint; range : integer);
begin
  randseed := seed;
  writeln(random(range), ' ', randseed);
end;

procedure randIntInterval(start_seed, end_seed : longint; range : integer);
var
  i : longint;
begin
  for i:=start_seed to end_seed do
  begin
    randIntSingle(i, range);
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
  start_seed, end_seed : longint;
  range, error : integer;
begin
  if ((paramcount < 2) or (paramcount > 3)) then
  begin
    writeln('Invalid number of arguments.');
    writeln('Usage: randInt seed range - generates one int in the interval [0, range)');
    writeln('Usage: randInt start_seed end_seed range - generates one int from each seed');
    halt(1);
  end;
  if (paramcount = 2) then
  begin
    val(paramstr(1), start_seed, error);
    checkerror(error, paramstr(1));
    val(paramstr(2), range, error);
    checkerror(error, paramstr(2));
    randIntSingle(start_seed, range);
  end
  else begin
    val(paramstr(1), start_seed, error);
    checkerror(error, paramstr(1));
    val(paramstr(2), end_seed, error);
    checkerror(error, paramstr(2));
    val(paramstr(3), range, error);
    checkerror(error, paramstr(3));
    randIntInterval(start_seed, end_seed, range);
  end;
end.