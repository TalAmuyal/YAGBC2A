package open_source.amuyal_tal.yagbc2a;

import open_source.amuyal_tal.yagbc2a.utils.BytesArray;

public final class ObjectFile
{
	private final SymbolTable _symbolTable;
	private final BytesArray _code;
	private final BytesArray _data;

	public ObjectFile()
	{
		_symbolTable = new SymbolTable();
		_code = new BytesArray();
		_data = new BytesArray();
	}

	public int appendCode(final BytesArray code)
	{
		final int address = _code.getSize();

		_code.append(code);

		return address;
	}

	public SymbolTable getSymbolTable()
	{
		return _symbolTable;
	}

	public BytesArray getCodeSegmentSection(
			final int offset,
			final int size
			)
	{
		return _code.getSubArray(offset, size);
	}

	public BytesArray getDataSegmentSection(
			final int offset,
			final int size
			)
	{
		return _data.getSubArray(offset, size);
	}

	public int getCodeSegmentSize()
	{
		return _code.getSize();
	}

	public int getDataSegmentSize()
	{
		return _data.getSize();
	}

	public void appendCodeSegment(
			final byte code
			)
	{
		_data.append(code);
	}

	public void appendDataSegment(
			final byte data
			)
	{
		_data.append(data);
	}

	public void setCodeSegmentSection(
			final int startIndex,
			final byte[] data
			)
	{
		for(int i = 0; i < data.length; i++)
		{
			_code.setAt(startIndex + i, data[i]);
		}
	}
}