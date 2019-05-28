package gec.scf.dummy.common.serialize;

import com.fasterxml.jackson.databind.util.StdConverter;

import io.seruco.encoding.base62.Base62;

public class GecEncodeSerialize extends StdConverter<String, String> {
	@Override
	public String convert(String value) {
		if (value != null){
			Base62 base62 = Base62.createInstance();
			byte[] encoded = base62.encode(value.getBytes());
			value =  new String(encoded);
		}
		return value;
	}
}
