{$N+,E-}
{$D-,L-,Y-}
program randDbl;
{$APPTYPE CONSOLE}
{dcc32 -B -CC -U..\Lib randDbl.pas}

function doubleToHexStr(input : double) : string;
const
  hexchars : array [$0..$F] of char = '0123456789ABCDEF';
var
  theresult : string[16];
  temp : array [1..8] of byte;
  i : byte;
begin
  move(input, temp, sizeof(temp));

  theresult := '';
  for i:= 8 downto 1 do
  begin
    theresult := theresult + hexchars[temp[i] div 16] + hexchars[temp[i] mod 16];
  end;
  doubleToHexStr := theresult;
end;

procedure randDoubleSingle(seed : longint);
var
  rand : double;
begin
  randseed := seed;
  rand := random;
  writeln(rand: 17 : 16, ' ', doubleToHexStr(rand), ' ', randseed);
end;

procedure randDoubleInterval(start_seed, end_seed : longint);
var
  i : longint;
begin
  for i:=start_seed to end_seed do
  begin
    randDoubleSingle(i);
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
  error : integer;
begin
  if ((paramcount < 1) or (paramcount > 2)) then
  begin
    writeln('Invalid number of arguments.');
    writeln('Usage: randDbl seed - generates one double in the interval [0, 1)');
    writeln('Usage: randDbl start_seed end_seed - generates one double from each seed');
    halt(1);
  end;
  if (paramcount = 1) then
  begin
    val(paramstr(1), start_seed, error);
    checkerror(error, paramstr(1));
    randDoubleSingle(start_seed);
  end
  else begin
    val(paramstr(1), start_seed, error);
    checkerror(error, paramstr(1));
    val(paramstr(2), end_seed, error);
    checkerror(error, paramstr(2));
    randDoubleInterval(start_seed, end_seed);
  end;
end.