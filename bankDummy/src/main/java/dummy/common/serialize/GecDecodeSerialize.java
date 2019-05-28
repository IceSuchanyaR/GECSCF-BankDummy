package gec.scf.dummy.common.serialize;

import com.fasterxml.jackson.databind.util.StdConverter;

import io.seruco.encoding.base62.Base62;

public class GecDecodeSerialize extends StdConverter<String, String> {
	
	@Override
	public String convert(String value) {
		if (value != null){
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(value.getBytes());
			value = new String(decoded);
		}
		return value;
	}

}
