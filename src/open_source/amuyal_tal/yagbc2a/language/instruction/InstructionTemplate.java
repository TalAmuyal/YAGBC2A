/*
    Yet Another Gameboy Classic/Color Assembler (YAGBC2A) can compile Gameboy-compatible images.
    Copyright (C) 2015  Tal Amuyal

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

	Contact information (email): TalAmuyal@gmail.com
*/

package open_source.amuyal_tal.yagbc2a.language.instruction;

import open_source.amuyal_tal.yagbc2a.OperandDataBase;
import open_source.amuyal_tal.yagbc2a.language.operand.Flag;
import open_source.amuyal_tal.yagbc2a.language.operand.Operand;
import open_source.amuyal_tal.yagbc2a.parsing.CommandTokens;
import open_source.amuyal_tal.yagbc2a.utils.Utils;
import open_source.amuyal_tal.yagbc2a.utils.adt.BytesArray;
import open_source.amuyal_tal.yagbc2a.utils.adt.MutableBytesArray;

public final class InstructionTemplate
{
	private static String getCommandName(final String command)
	{
		String name = null; //TODO: Implement

		switch(command)
		{
			case "NOP":
			{
				name = "No operation";
			}
			break;

			case "LD":
			{
				name = "Load";
			}
			break;

			case "INC":
			{
				name = "Increase";
			}
			break;

			case "DEC":
			{
				name = "Decrease";
			}
			break;

			case "ADD":
			{
				name = "Add";
			}
			break;

			case "RLCA":
			{
				name = "";
			}
			break;

			case "RRCA":
			{
				name = "";
			}
			break;

			case "RRA":
			{
				name = "";
			}
			break;

			case "RLA":
			{
				name = "";
			}
			break;

			case "JR":
			{
				name = "Jump-relative";
			}
			break;

			case "STOP":
			{
				name = "Stop";
			}
			break;

			case "DAA":
			{
				name = "Decimal Adjust A";
			}
			break;

			case "CPL":
			{
				name = "";
			}
			break;

			case "CCF":
			{
				name = "";
			}
			break;

			case "SCF":
			{
				name = "";
			}
			break;

			case "HALT":
			{
				name = "Halt";
			}
			break;

			case "ADC":
			{
				name = "Add (with) carry";
			}
			break;

			case "SUB":
			{
				name = "Subtract";
			}
			break;

			case "SBC":
			{
				name = "Subtract (with) carry";
			}
			break;

			case "AND":
			{
				name = "And";
			}
			break;

			case "XOR":
			{
				name = "Exclusive or";
			}
			break;

			case "OR":
			{
				name = "Inclusive or";
			}
			break;

			case "CP":
			{
				name = "Compare";
			}
			break;

			case "RET":
			{
				name = "Return";
			}
			break;

			case "LDH":
			{
				name = "";
			}
			break;

			case "RETI":
			{
				name = "Return interrupt";
			}
			break;

			case "POP":
			{
				name = "Pop";
			}
			break;

			case "PUSH":
			{
				name = "Push";
			}
			break;

			case "JP":
			{
				name = "Jump";
			}
			break;

			case "CALL":
			{
				name = "Call";
			}
			break;

			case "DI":
			{
				name = "Disable interrupts";
			}
			break;

			case "EI":
			{
				name = "Enable interrupts";
			}
			break;

			case "RST":
			{
				name = "Reset";
			}
			break;

			case "RLC":
			{
				name = "";
			}
			break;

			case "RRC":
			{
				name = "";
			}
			break;

			case "RL":
			{
				name = "Rotate left";
			}
			break;

			case "RR":
			{
				name = "Rotate right";
			}
			break;

			case "SLA":
			{
				name = "";
			}
			break;

			case "SRA":
			{
				name = "";
			}
			break;

			case "SWAP":
			{
				name = "";
			}
			break;

			case "SRL":
			{
				name = "";
			}
			break;

			case "BIT":
			{
				name = "";
			}
			break;

			case "RES":
			{
				name = "";
			}
			break;

			case "SET":
			{
				name = "Set";
			}
			break;
		}

		assert(name != null);
		assert(name != "");

		return name;
	}

	private final int _code;
	private final String _command;
	private final String _name;
	private final int _size;
	private final int _cycles;
	private final FlagInfluance[] _flagsInfluance;
	private final Operand[] _parameters;

	public InstructionTemplate(
			final int code,
			final String command,
			final int cycles,
			final FlagInfluance zeroInfluance,
			final FlagInfluance negativeInfluance,
			final FlagInfluance halfCarryInfluance,
			final FlagInfluance carryInfluancey,
			final String parameters)
	{
		_code = code;
		_command = command;
		_name = getCommandName(command);
		_cycles = cycles;

		int size = (code <= 0xFF) ? 1 : 2; //Base size

		_flagsInfluance = new FlagInfluance[4];
		_flagsInfluance[0] = zeroInfluance;
		_flagsInfluance[1] = negativeInfluance;
		_flagsInfluance[2] = halfCarryInfluance;
		_flagsInfluance[3] = carryInfluancey;

		final String[] parametersList =  parameters.isEmpty() ? new String[0] : parameters.split(",");
		_parameters = new Operand[parametersList.length];
		for(int i = 0; i < parametersList.length; i++)
		{
			_parameters[i] = OperandDataBase.searchByName(parametersList[i]);
			if(_parameters[i] == null)
			{
				Utils.abort("unrecognized parameter \'" + parametersList[i] + "\'");
			}

			size += _parameters[i].getCodeSize();
		}

		_size = size;
	}

	final public String getCommand()
	{
		return _command;
	}

	final public String getName()
	{
		return _name;
	}

	public int getSize()
	{
		return _size;
	}

	final public FlagInfluance getFlagsInfluance(final Flag flag)
	{
		return _flagsInfluance[flag.getIndex()];
	}

	public int getParametersCount()
	{
		return _parameters.length;
	}

	public Operand getParameter(final int parameterIndex)
	{
		return _parameters[parameterIndex];
	}

	public int getExecutionCycles()
	{
		return _cycles;
	}

	public boolean matches(final CommandTokens tokens)
	{
		if(_command.equalsIgnoreCase(tokens.getCommand()) == false)
		{
			return false; //Doesn't match
		}

		if(_parameters.length != tokens.getArgumentsCount())
		{
			return false; //Doesn't match
		}

		for(int i = 0; i < _parameters.length; i++)
		{
			if(_parameters[i].matches(tokens.getArgument(i)) == false)
			{
				return false;
			}
		}

		return true;
	}

	public boolean betterThan(final InstructionTemplate bestMatch)
	{
		return false; //TODO: Implement instruction-template comparison
	}


	public BytesArray assemble(final CommandTokens tokens)
	{
		final BytesArray code = new MutableBytesArray();

		if(_code <= 0xFF)
		{
			code.append((byte)_code);
		}
		else //(0xFF <= _code <= 0xFFFF)
		{
			code.append((byte)(_code >> 8));
			code.append((byte)(_code & 0xFF));
		}

		for(int i = 0; i < _parameters.length; i++)
		{
			final BytesArray parameterCode = _parameters[i].assemble(tokens.getArgument(i));
			if(parameterCode != null)
			{
				code.append(parameterCode);
			}
		}

		return code;
	}

//TODO: Implement (disassemble)
/*
	public CommandTokens disassemble(final byte[] machineCode)
	{
		final byte[] codeAarray = Utils.toByteArray(_code);

		if(Utils.compareByteArray(codeAarray, machineCode) == false)
		{
			return null; //Doesn't match
		}

		String parameters = "";
		int bytesParsed = codeAarray.length;

		for(final Operand parameter : _parameters)
		{
			final byte[] segmentedMachineCode = Arrays.copyOfRange(machineCode, bytesParsed, machineCode.length);
			final String disassembledParameter = parameter.disassemble(segmentedMachineCode);
			if(disassembledParameter == null)
			{
				return null; //Doesn't match
			}

			if(parameters.isEmpty())
			{
				parameters = disassembledParameter;
			}
			else
			{
				parameters += "," + disassembledParameter;
			}

			bytesParsed += parameter.getCodeSize();
		}

		try
		{
			return new CommandTokens(getCommand() + " " + parameters);
		}
		catch(final SyntaxException e)
		{
			Utils.abort(e);
		}

		return null;
	}
*/
}
