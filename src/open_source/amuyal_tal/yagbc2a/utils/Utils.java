package open_source.amuyal_tal.yagbc2a.utils;

import open_source.amuyal_tal.yagbc2a.BootHeader;
import open_source.amuyal_tal.yagbc2a.InternalErrorException;
import open_source.amuyal_tal.yagbc2a.ObjectFile;
import open_source.amuyal_tal.yagbc2a.SymbolTable;
import open_source.amuyal_tal.yagbc2a.utils.adt.BytesArray;

public final class Utils
{
	public static String stringFromSymbol(
			final BytesArray bytes
			)
	{
		String string = "";

		for(int i = 0; i < bytes.getSize() && bytes.getAt(i) != '\0'; i++)
		{
			string += (char)bytes.getAt(i);
		}

		return string;
	}

	public static int getSymbolAbsoluteLocation(
			final String symbolName,
			final ObjectFile objectFile
			)
	{
		final SymbolTable symbolTable = objectFile.getSymbolTable();

		final int addressOffset = symbolTable.isSymbolicLabelDefined(symbolName) ?
				(objectFile.getDataSegmentSize() + symbolTable.getSymbolicLabelAddress(symbolName)) :
					symbolTable.getSymbolicVariable(symbolName).getAddress();

		return BootHeader.getSize() + addressOffset;
	}

	public static boolean isSignedByte(final String stringPotentialValue)
	{
		boolean result;

		try
		{
			final int value = Integer.parseInt(stringPotentialValue);

			result = (-128 <= value) && (value <= 127);
		}
		catch(final NumberFormatException ex)
		{
			result = false;
		}

		return result;
	}

	public static byte[] toByteArray(
			final int integer
			)
	{
		Utils.assertCondition(0 <= integer && integer <= 0xFFFF);

		byte[] bytes = null;

		if(integer <= 0xFF)
		{
			bytes = new byte[1];
			bytes[0] = (byte)integer;
		}
		else //if(integer <= 0xFFFF)
		{
			bytes = new byte[2];
			bytes[0] = (byte)(integer >> 8);
			bytes[1] = (byte)(integer);
		}

		Utils.assertCondition(bytes != null);

		return bytes;
	}

	public static byte[] getOtherEndianess(final byte[] original)
	{
		final byte[] reordered = new byte[original.length];

		for(int i = 0; i < reordered.length; i++)
		{
			reordered[i] = original[original.length - i - 1];
		}

		return reordered;
	}

	public static int neededSize(
			int value
			)
	{
		int neededSize = 0;

		while(value > 0)
		{
			neededSize++;
			value >>= 8;
		}

		return Integer.max(1, neededSize);
	}

	public static int parseValue(String string) throws NumberFormatException
	{
		int base = 10; //Decimal is the default base
		boolean baseDetected = false;

		if(string.length() > 1)
		{
			final char postFix = Character.toLowerCase(string.charAt(string.length() - 1));
			if(postFix == 'h')
			{
				baseDetected = true;
				base = 16;
				string = string.substring(0, string.length() - 1);
			}
			else if(postFix == 'b')
			{
				baseDetected = true;
				base = 2;
				string = string.substring(0, string.length() - 1);
			}
			else if(postFix == 'd')
			{
				baseDetected = true;
				//base = 10; already set as this is the default
				string = string.substring(0, string.length() - 1);
			}
		}

		if(baseDetected == false && string.length() > 2)
		{
			final String prefix = string.substring(0, 2);
			if(prefix.equals("0x"))
			{
				baseDetected = true;
				base = 16;
				string = string.substring(2);
			}
			else if(prefix.equals("0b"))
			{
				baseDetected = true;
				base = 2;
				string = string.substring(2);
			}
			else if(prefix.equals("0d"))
			{
				baseDetected = true;
				//base = 10; already set as this is the default
				string = string.substring(2);
			}
		}

		return Integer.parseInt(string, base);
	}

	public static void displayError(final Throwable throwable)
	{
		System.out.println("Error: " + throwable.getLocalizedMessage());
		throwable.printStackTrace();
		if(throwable.getCause() != null)
		{
			System.out.println("Cause: " + throwable.getCause());
		}
	}

	public static void abort(final String reason)
	{
		Utils.displayError(new InternalErrorException(new Exception(reason)));
		abort();
	}

	public static void abort(final Throwable throwable)
	{
		Utils.displayError(new InternalErrorException(throwable));
		abort();
	}

	public static void assertCondition(final boolean condition)
	{
		if(condition == false)
		{
			abort("Assertion failed");
		}
	}

	public static void ilegalOperation()
	{
		Utils.abort("Ilegal operation");
	}

	private static void abort()
	{
		//TODO: Add cleanups like deleting temporary or incomplete files

		System.exit(-1);
	}
}
