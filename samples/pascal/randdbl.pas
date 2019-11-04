{$IFDEF FPC}
  {$mode objfpc} {$H+}
{$ELSE}
  {$N+,E-,D-,L-,Y-}
{$ENDIF}

Program random_double;
{$IFDEF CONDITIONALEXPRESSIONS}
  {$IF CompilerVersion >= 14}
    {$APPTYPE CONSOLE}
  {$IFEND}
{$ENDIF}
{dcc32 -B -CC -U..\Lib randDbl.pas}

Function doubleToHexStr(input : double) : string;

Const 
  hexchars : array [$0..$F] Of char = '0123456789ABCDEF';

Var 
  theresult : string[16];
  temp : array [1..8] Of byte absolute input;
  i : byte;
Begin
  {we do not need the following line if we use the absolute statement}
  {move(input, temp, sizeof(temp));}

  theresult := '';
  For i:= 8 Downto 1 Do
    Begin
      theresult := theresult + hexchars[temp[i] Div 16] + hexchars[temp[i] Mod
                   16];
    End;
  doubleToHexStr := theresult;
End;

Procedure randDouble(seed : longint; count : integer);

Var 
  rand : double;
  i : integer;
Begin
  randseed := seed;
  For i:=1 To count Do
    Begin
      rand := random;
    {$IFDEF FPC}
      writeln(rand: 17 : 16, ' ', doubleToHexStr(rand));
    {$ELSE}
      writeln(rand: 17 : 16, ' ', doubleToHexStr(rand), ' ', randseed);
    {$ENDIF}
    End;
End;

Procedure checkerror(error : integer; argument : String);
Begin
  If (error <> 0) Then
    Begin
      writeln('invalid argument ', argument);
      halt(2);
    End;
End;

Var 
  seed : longint;
  count : integer;
  error : integer;
Begin
  If ((paramcount < 1) Or (paramcount > 2)) Then
    Begin
      writeln('Invalid number of arguments.');
      writeln(
             'Usage: randDbl seed - generates one double in the interval [0, 1)'
      );
      writeln(
  'Usage: randDbl seed count - generates <count> doubles in the interval [0, 1)'
      );
      halt(1);
    End;

  val(paramstr(1), seed, error);
  checkerror(error, paramstr(1));

  count := 1;
  If (paramcount = 2) Then
    Begin
      val(paramstr(2), count, error);
      checkerror(error, paramstr(2));
    End;
  randDouble(seed, count);
End.
