{$IFDEF FPC}
  {$mode objfpc} {$H+}
{$ELSE}
  {$N+,E-,D-,L-,Y-}
{$ENDIF}
program randdbl;
{$IFDEF CONDITIONALEXPRESSIONS}
  {$IF CompilerVersion >= 14}
    {$APPTYPE CONSOLE}
  {$IFEND}
{$ENDIF}
{dcc32 -B -CC -U..\Lib randDbl.pas}

function doubleToHexStr(input : double) : string;
const
  hexchars : array [$0..$F] of char = '0123456789ABCDEF';
var
  theresult : string[16];
  temp : array [1..8] of byte absolute input;
  i : byte;
begin
  {we do not need the following line if we use the absolute statement}
  {move(input, temp, sizeof(temp));}

  theresult := '';
  for i:= 8 downto 1 do
  begin
    theresult := theresult + hexchars[temp[i] div 16] + hexchars[temp[i] mod 16];
  end;
  doubleToHexStr := theresult;
end;

procedure randDouble(seed : longint; count : integer);
var
  rand : double;
  i : integer;
begin
  randseed := seed;
  for i:=1 to count do
  begin
    rand := random;
    {$IFDEF FPC}
    writeln(rand: 17 : 16, ' ', doubleToHexStr(rand));
    {$ELSE}
    writeln(rand: 17 : 16, ' ', doubleToHexStr(rand), ' ', randseed);
    {$ENDIF}
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
  count : integer;
  error : integer;
begin
  if ((paramcount < 1) or (paramcount > 2)) then
  begin
    writeln('Invalid number of arguments.');
    writeln('Usage: randDbl seed - generates one double in the interval [0, 1)');
    writeln('Usage: randDbl seed count - generates <count> doubles in the interval [0, 1)');
    halt(1);
  end;

  val(paramstr(1), seed, error);
  checkerror(error, paramstr(1));

  count := 1;
  if (paramcount = 2) then
  begin
    val(paramstr(2), count, error);
    checkerror(error, paramstr(2));
  end;
  randDouble(seed, count);
end.
