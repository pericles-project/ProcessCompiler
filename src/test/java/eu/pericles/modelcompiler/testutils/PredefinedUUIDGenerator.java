package eu.pericles.modelcompiler.testutils;

import eu.pericles.modelcompiler.common.UUIDGeneration;

public class PredefinedUUIDGenerator implements UUIDGeneration {

	private int identifier = 0;
	private static final String[] identifiersList = { "eddcf455-65e9-4a00-9261-76b857a129cc", "46de0f8e-3a31-4714-969d-bf64667da87a",
			"a6dd4fa8-8b3a-475f-ae56-2ce23fe0db82", "44812f64-491a-4c03-b35b-579b90ebb452", "aedc4cd2-beb0-4e92-9577-f45b029435d1",
			"c79a9779-623b-428f-9581-3e14358376bc", "e99688e2-6215-4d15-bbc4-efe9acfe5907", "e8dd3442-b79a-4b51-9cb1-21757dbe5784",
			"f4f37ed5-8a4f-4a4e-a400-122ecd179e12", "1703e740-93d7-499c-8906-478dfc7be5fa", "4891d654-0152-41b2-b882-5e17b2d07306",
			"ab29b653-115c-4922-8f6c-5ff7979d212b", "1ae4f07c-5919-46f9-9dfa-0be989976b07", "13d89fc6-9f63-41d0-b606-f74e7cd8d39d",
			"e56069b6-b25b-4f6a-bb07-05cfe5c48261", "9adc846d-a443-4dc5-8f67-dfbd4ca864cb", "8a4781f3-0e39-4bfd-b86d-635202b91c54",
			"6865662f-9835-4561-8c9f-f692337ad8a8", "7e650cc3-6bae-44c5-82b5-d6db4166b46f", "bc696593-5d07-4cb6-8819-ac6b28c9aa51",
			"d9536d51-62d1-40bc-87c9-5997855ec765", "6e7335dd-1b4e-4a95-ac0a-06293b4ea116", "4927dc09-bfa8-43cd-83fb-cc56ced37ee5",
			"f383fb27-361f-4ef9-b6a6-906e483edcd8", "f78b099a-9b59-4d1a-a63f-bd70467fae12", "c1628502-d47e-49d1-9753-f55841f18b6d",
			"4352ed23-76fa-48c0-bec4-720063159031", "81cffa4c-2edb-4799-b406-e7d841587faf", "5febf5b7-b4fe-481a-9b97-cbc3ce62f112",
			"fe2be680-3c43-4862-9d26-f7907c865c46", "93e895f2-88b2-49bb-aa39-dec46002197e", "614a32c7-66c6-495c-8666-e06881fb5ad7",
			"6de347c5-0480-4808-b0b2-4e1b9cb126ac", "8e68480a-72e7-4171-b7d7-379c2055a450", "d6a29f77-68ab-435a-956d-061e6b019d2e",
			"a3ce820a-b79a-42ce-a949-3fa9d544d3bb", "79c5ae93-c55f-4f95-bb9e-836c1c05b334", "1165cbfb-d29b-4dd0-be1a-de9dab48d33a",
			"515bee7e-f778-4d51-a2b8-0410f685175a", "0d81abdc-66f0-4a35-8987-0cfa3e0027f0", "81d592a9-a56b-4cb5-918c-7c8b20b748be",
			"f3b4c7f3-4e52-4fe5-8df9-950603ddf8bf", "6ffaa061-b9c3-406e-86d9-acac2d9999b3", "a94c6487-e87a-48cc-aacd-04483b4b45a6",
			"71e05039-a1b2-41db-8255-852015ac9621", "2077fa30-d077-4f17-ad66-d58f0a7a2ab1", "a0055a5e-365d-4679-9177-4bb49eaf0709",
			"0e9317bc-664c-4684-ab41-ebf8bba8094c", "cd7e101f-a5ed-408a-8b90-a04deff59560", "5c58e375-4918-4b03-845b-a17062eae35d",
			"500f4e63-a323-47c9-ae05-2b50bfad8396", "4744daba-fd3a-4497-b9d9-77d38c6be1d8", "96e01182-00ed-4bd0-aaa5-581dbbc5d50e",
			"0b421ee6-2c15-49bd-9c4e-662e6e4e32f4", "b1ddc1c2-d0b4-4c9e-ac06-91f385b13ca9", "f40b759b-75d5-4b71-9eee-44f8479cb034",
			"3a305a5f-75e3-428f-8e49-a7f7b86d6ec6", "97108a04-88ac-4fb2-bfbe-775170e313ac", "2905bb67-72b2-48d2-890a-7f346a53d355",
			"eea26312-4f14-4c34-9a85-6aa7cf12cccf", "8773c1f2-993f-425c-82e6-319ae30eefb4", "a0a7d932-466c-4657-8559-1da9f695e02b",
			"0a8915fb-7886-44f2-83bf-0a1ebca23e78", "f7fd97eb-0a35-44df-8efa-9280b821a12c", "03dd5dff-bdcf-44e5-8e87-04c394398e82",
			"c9e177a0-b771-4214-930f-fccc6f3c3d1f", "47a1e839-6c82-423c-83cc-be419298ffb4", "51ed33e1-7d00-492e-9ce7-9027be0d2437",
			"55974b78-36c1-4bd4-9391-03dbfc6d1284", "6492b992-94f4-45b3-a2e0-81b8d065f7a3", "4b0c52ae-28a5-4b3e-ab54-0aafcd9e5abe",
			"1e639800-1f13-4841-a3aa-67b2c8fe5737", "bd656fe4-0267-469c-9cff-36c445676e27", "e7578ee6-7a9e-4f21-b7eb-2c9838013a7a",
			"f109fa03-1ca9-4868-8f35-50c43f6a8690", "661de80f-9baf-4d30-bf26-1eb17e0f0160", "bdd32458-559c-4bff-a97f-8d419f1490ce",
			"b91ae8ba-508a-44cd-b363-08bd26a4ed17", "cf2c65dc-998a-435c-b530-e1e767accb0c", "2406759a-e23f-471a-9ded-598f5f522889",
			"6dd8ae56-9485-4417-a900-22a02a0b0f03", "8ab420ff-9d4c-4510-adf8-c1e2de9a3efc", "02fc9da8-3fbc-4766-be9a-d1420d691391",
			"881db35c-d5b1-4b51-9dc9-25c18cef30e1", "a3baeb7d-bb16-47c0-b7ed-a399613c94ed", "de941eb1-b849-4b34-8a14-bfa5dc4b8be7",
			"9a221875-990c-4752-bfd9-ecabf4f6b81b", "a8e7d38c-bc7c-49b0-9e1b-579751e5b289", "07c8e337-a8a3-47c6-8274-ac07f911abd4",
			"cf9bd7fc-381c-4bed-817a-ff8189b5859f", "016e7a26-9342-49fe-bf90-4eeaa5bf1808", "7579aa82-e7cb-43d3-bb88-9e6a71f01251",
			"89bf73a6-5078-4bef-af5d-a08daf33c165", "efa5e393-0e9f-49a9-add8-be7536aed590", "15e1e4b9-b1ad-44fd-936b-1a09f0009154",
			"2b54a701-a798-4426-86ef-a475173bdc5b", "a3ab2513-e50a-49ee-95cf-f56a5deaeb6d", "29a9e34d-f1fc-494f-97cb-4f0497367c98",
			"83920271-3872-4abd-bfa9-52d22e10352a", "faac35e6-fa4e-4c13-ad02-e40932ac9a39" };

	@Override
	public String requestUUID() {
		String uniqueIdentifier = identifiersList[identifier];
		identifier++;

		return uniqueIdentifier;
	}

}
